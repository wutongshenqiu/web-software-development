package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IFormDao;
import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import com.example.apiservice.domain.entity.*;
import com.example.apiservice.event.FormCreateEvent;
import com.example.apiservice.exception.form.QueryFormException;
import com.example.apiservice.exception.form.SubmitFormException;
import com.example.apiservice.service.*;
import com.example.apiservice.type.enumration.FormStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher applicationEventPublisher;

    public FormDoServiceImpl(final IFormDao formDao,
                             final RabbitTemplate rabbitTemplate,
                             final IBuildingService buildingService,
                             final IUserService userService,
                             final IGroupService groupService,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.formDao = formDao;
        this.rabbitTemplate = rabbitTemplate;
        this.buildingService = buildingService;
        this.userService = userService;
        this.groupService = groupService;
        this.applicationEventPublisher = applicationEventPublisher;
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
        if (form.isEmpty()) throw new QueryFormException("???????????????????????????????????????");

        return form.get();
    }

    @Transactional
    @Override
    public Form submitForm(Long userId, Long groupId, Long buildingId) throws SubmitFormException {
        User user = userService.findOrElseRaise(userId);
        // 1. ??????????????????
        Building building = buildingService.findOrElseRaise(buildingId);
        // 2. ?????????
        Group group = groupService.findActiveGroupByUserId(userId);
        // 3. ??????????????????
        if (!group.getId().equals(groupId)) {
            throw new SubmitFormException("???????????????????????????????????????");
        }

        Form form = Form.builder()
                .submitter(user)
                .group(group)
                .building(building)
                .formStatus(FormStatus.CREATED)
                .build();
        saveAndFlush(form);

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

        final FormCreateEvent event = new FormCreateEvent(this, formInRabbitDto);
        applicationEventPublisher.publishEvent(event);

        return form;
    }
}
