import random
import copy
from typing import Optional
from datetime import datetime
import json

import httpx

from pydantic import BaseModel, BaseSettings, AnyHttpUrl, Field

import pymysql
from pymysql import Connection

import pika

from faker import Faker

faker = Faker('zh-CN')


class GroupMember(BaseModel):
    id: int
    name: str
    gender: int
    

class Group(BaseModel):
    id: int
    creator_id: int
    gender: int
    group_members: list[GroupMember]
    name: str = Field(default_factory=lambda : faker.word())
    description: str = Field(default_factory=lambda : faker.unique.text())
    create_time: datetime = Field(default_factory=lambda : datetime.now())
    update_time: datetime = Field(default_factory=lambda : datetime.now())
    group_status: int = 0
    invite_code: str = Field(default_factory=lambda : faker.unique.password(20))
    
    
class Order(BaseModel):
    id: int
    building_id: int
    group_id: int
    gender: int
    submitter_id: int
    group_members: list[GroupMember]
    create_time: datetime = Field(default_factory=lambda : datetime.now())
    update_time: datetime = Field(default_factory=lambda : datetime.now())
    form_status: int = 0
    

class APIEndpoint(BaseSettings):
    base: AnyHttpUrl = 'http://localhost:39010'
    test: AnyHttpUrl = f'{base}/test'
    
    test_users: AnyHttpUrl = f'{test}/users'
    test_empty_beds: AnyHttpUrl = f'{test}/emptybeds'
    
api = APIEndpoint()


def _fetch_all_users() -> list[dict]:
    r = httpx.get(api.test_users)
    
    return r.json()


def _fetch_all_empty_beds() -> list[dict]:
    r = httpx.get(api.test_empty_beds)
    
    return r.json()


def _make_groups(
        users: list[dict], 
        num_member_limit: tuple[int, int] = (1, 4)) -> tuple[list[Group], list[Group]]:
    group_idx = 1
    
    def _make_groups_internal(users_: list[dict]) -> list[Group]:
        nonlocal group_idx

        min_num_member, max_num_member = num_member_limit
        assert min_num_member <= max_num_member
        
        groups: list[Group] = []
        idx = 0
        while idx < len(users_):
            num_member = random.randint(min_num_member, 
                                        min(max_num_member, len(users_) - idx))
            group_members = [
                GroupMember(id=user['id'], 
                            name=user['name'],
                            gender=user['gender']) 
                for user in users_[idx:idx+num_member]
            ]
            groups.append(
                Group(
                    id=group_idx,
                    creator_id=random.choice(
                        [member.id for member in group_members]),
                    gender=group_members[0].gender,
                    group_members=group_members)
                )

            group_idx += 1
            idx += num_member
        
        return groups
    
    female_users = [
        user for user in users
        if user['gender'] == 0
    ]
    male_users = [
        user for user in users
        if user['gender'] == 1
    ]
    
    return (_make_groups_internal(female_users), 
            _make_groups_internal(male_users))


def _make_order_for_groups(groups: list[Group], empty_beds: list[dict]) -> list[Order]:
    empty_beds = copy.deepcopy(empty_beds)
    female_empty_beds = [
        bed for bed in empty_beds
        if bed['gender'] == 0
    ]
    male_empty_beds = [
        bed for bed in empty_beds
        if bed['gender'] == 1
    ]
    
    def _make_order(group: Group, 
                    order_id: int) -> Optional[Order]:
        def _make_order_internal(empty_beds_: list[dict]) -> Optional[Order]:
            for eb in empty_beds_:
                if eb["cnt"] > len(group.group_members) + 4:
                    eb["cnt"] -= len(group.group_members)
                    return Order(id=order_id,
                                 submitter_id=group.creator_id,
                                 gender=group.group_members[0].gender,
                                 building_id=eb['building_id'],
                                 group_id=group.id,
                                 group_members=group.group_members)
            
        match group.gender:
            case 0:
                return _make_order_internal(female_empty_beds)
            case 1:
                return _make_order_internal(male_empty_beds)    

    order_idx = 1
    orders: list[Order] = []
    for group in groups:
        order = _make_order(group, order_idx)
        if order is not None:
            order_idx += 1
            orders.append(order)
            
    return orders


def _write_groups_to_mysql(groups: list[Group], conn: Connection) -> None:
    sql = """
    INSERT INTO `tb_group`
    (`id`, `create_time`, `update_time`, `description`, `group_status`, 
    `invite_code`, `name`, `creator_id`)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""
    
    with conn.cursor() as cursor:
        for group in groups:
            cursor.execute(
                sql, 
                (group.id, group.create_time, group.update_time, 
                    group.description, group.group_status,
                    group.invite_code, group.name, group.creator_id))
    
    conn.commit()


def _write_order_to_mysql(orders: list[Order], conn: Connection) -> None:
    sql = """
    INSERT INTO `tb_form`
    (`id`, `create_time`, `update_time`, `form_status`, `building_id`, 
    `group_id`, `submitter_id`)
    VALUES (%s, %s, %s, %s, %s, %s, %s)"""
    
    with conn.cursor() as cursor:
        for order in orders:
            cursor.execute(
                sql, 
                (order.id, order.create_time, order.update_time, 
                    order.form_status, order.building_id,
                    order.group_id, order.submitter_id))
    
    conn.commit()


if __name__ == '__main__':
    N_USERS = 1000
    
    from rich.console import Console
    
    console = Console(
        log_path=False, 
        log_time=True, 
        log_time_format="[%H:%M:%S:%f]")
    
    console.log("Stress testing for order consumer service")
    
    users = _fetch_all_users()[:N_USERS]
    console.log(f"Fetching information of {len(users)} users")

    female_groups, male_groups = _make_groups(users)
    console.log(f"Divide {len(users)} users into "
                f"{len(female_groups)} female groups and "
                f"{len(male_groups)} male groups")
    
    empty_beds = _fetch_all_empty_beds()
    console.log(f"Fetching information of empty beds")
    
    orders = _make_order_for_groups(groups=female_groups + male_groups, 
                                    empty_beds=empty_beds)
    console.log(f"Creating {len(orders)} orders")
    
    mysql_conn = pymysql.connect(
        host='localhost',
        port=39001,
        user='test',
        password='123test321',
        database='test',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor)
    console.log('Connecting to mysql')
    
    with mysql_conn as conn:
        _write_groups_to_mysql(female_groups + male_groups, conn)
        console.log('Writing groups to mysql')
        
        _write_order_to_mysql(orders, conn)
        console.log('Writing orders to mysql')
    
    rabbit_conn = pika.BlockingConnection(pika.ConnectionParameters(
        host='localhost',
        port=39004,
        credentials=pika.PlainCredentials(username='qiufeng',
                                          password='123456')))
    console.log('Connecting to rabbitmq')
    
    channel = rabbit_conn.channel()
    channel.queue_declare('order', durable=True)
    console.log('Connecting to order queue')
    
    for order in orders:
        order_in_rabbit = {
            'order_id': order.id,
            'building_id': order.building_id,
            'group_id': order.group_id,
            'group_members': [{
                               'id': group_member.id,
                               'name': group_member.name,
                               'gender': group_member.gender
                               } 
                              for group_member in order.group_members]
        }
        
        channel.basic_publish(exchange='',
                              routing_key='order',
                              body=json.dumps(order_in_rabbit))
    console.log('Sending all order to rabbit done')