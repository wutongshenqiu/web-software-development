package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IFormDao;
import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import com.example.apiservice.domain.entity.*;
import com.example.apiservice.exception.form.QueryFormException;
import com.example.apiservice.exception.form.SubmitFormException;
import com.example.apiservice.service.*;
import com.example.apiservice.type.enumration.FormStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FormDoServiceImpl extends BaseDoServiceImpl<Form, Long> implements IFormService {
    private final IFormDao formDao;
    private final RabbitTemplate rabbitTemplate;
    private final IBuildingService buildingService;
    private final IUserService userService;
    private final IGroupService groupService;

    public FormDoServiceImpl(final IFormDao formDao, final RabbitTemplate rabbitTemplate, final IBuildingService buildingService, final IUserService userService, final IGroupService groupService) {
        this.formDao = formDao;
        this.rabbitTemplate = rabbitTemplate;
        this.buildingService = buildingService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public IBaseDao<Form, Long> getBaseDao() {
        return this.formDao;
    }

    @Override
    public void sendFormToMQ(FormInRabbitDto formInRabbitDto) {
        rabbitTemplate.convertAndSend("order", formInRabbitDto);
    }

    @Override
    public List<Form> findAllBySubmitterId(Long submitterId) {
        return formDao.findAllBySubmitterId(submitterId);
    }

    @Override
    public Form findBySubmitterId(Long submitterId, Long formId) throws QueryFormException {
        Optional<Form> form = formDao.findBySubmitterIdAndId(submitterId, formId);
        if (form.isEmpty()) throw new QueryFormException("表单不存在或不属于用户创建");

        return form.get();
    }

    @Override
    public Form submitForm(Long userId, Long groupId, Long buildingId) throws SubmitFormException {
        User user = userService.findOrElseRaise(userId);
        // 1. 宿舍楼不存在
        Building building = buildingService.findOrElseRaise(buildingId);
        // 2. 未组队
        Group group = groupService.findActiveGroupByUserId(userId);
        // 3. 队伍号不匹配
        if (!group.getId().equals(groupId)) {
            throw new SubmitFormException("队伍号与用户所在队伍不匹配");
        }

        Form form = Form.builder()
                .submitter(user)
                .group(group)
                .building(building)
                .formStatus(FormStatus.CREATED)
                .build();
        save(form);

        List<GroupMember> groupMembers = groupService.findGroupMembersByGroupId(group.getId());
        List<User> users = new ArrayList<>();
        for (GroupMember groupMember : groupMembers) {
            users.add(groupMember.getMember());
        }

        FormInRabbitDto formInRabbitDto = new FormInRabbitDto()
                .setFormId(form.getId())
                .setGroupId(group.getId())
                .setBuildingId(building.getId())
                .setGroupMembersFromUsers(users);
        sendFormToMQ(formInRabbitDto);

        return form;
    }
}
