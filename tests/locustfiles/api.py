from typing import Optional
from enum import IntEnum
import random

from pydantic import BaseSettings, AnyHttpUrl, BaseModel

import httpx

from locust import HttpUser, task, between


class APIEndpoint(BaseSettings):
    base: AnyHttpUrl = 'http://localhost:39010'
    auth: AnyHttpUrl = f'{base}/auth'
    user: AnyHttpUrl = f'{base}/user'
    room: AnyHttpUrl = f'{base}/room'
    team: AnyHttpUrl = f'{base}/team'
    order: AnyHttpUrl = f'{base}/order'
    test: AnyHttpUrl = f'{base}/test'
    
    test_users: AnyHttpUrl = f'{test}/users'
    
    auth_login: AnyHttpUrl = f'{auth}/login'
    
    user_myinfo: AnyHttpUrl = f'{user}/myinfo'
    user_myroom: AnyHttpUrl = f'{user}/myroom'
    
    room_buildinglist: AnyHttpUrl = f'{room}/buildinglist'
    room_building: AnyHttpUrl = f'{room}/building'
    room_empty: AnyHttpUrl = f'{room}/empty'
    
    team_create: AnyHttpUrl = f'{team}/create'
    team_join: AnyHttpUrl = f'{team}/join'
    team_my: AnyHttpUrl = f'{team}/my'
    team_transfer: AnyHttpUrl = f'{team}/transfer'
    
    order_create: AnyHttpUrl = f'{order}/create'
    order_list: AnyHttpUrl = f'{order}/list'

api = APIEndpoint()
    

class StatusCode(IntEnum):
    OK = 200    


class UserModel(BaseModel):
    id: int
    name: str
    gender: int
    username: str
    password: str
    student_id: str


class UserPool:

    def __init__(self) -> None:
        self._users: list[UserModel] = []
        self._idx: int = 0
    
    def fetch_users(self) -> None:
        r = httpx.get(api.test_users)
        
        self._users = [UserModel(**user) for user in r.json()]
        self._idx = 0
        
        self._male = [x for x in self._users if x.gender == 1]
        self._male_idx: int = 0
        self._female = [x for x in self._users if x.gender == 0]
        self._female_idx: int = 0
        
    def get_one(self) -> Optional[UserModel]:
        if self._idx < len(self._users):
            user = self._users[self._idx]
            self._idx += 1
            return user
        
    def get_multi(self, n: int) -> Optional[list[UserModel]]:
        if (self._male_idx >= len(self._male)) \
                and (self._female_idx >= len(self._female)):
            return None

        if self._female_idx >= len(self._female):
            gender = 1
        elif self._male_idx >= len(self._male):
            gender = 0
        else:
            gender = random.randint(0, 1)

        idx = [self._female_idx, self._male_idx][gender]
        users = [self._female, self._male][gender]
        
        chosen = users[idx:min(idx+n, len(users))]
        match gender:
            case 0:
                self._female_idx += len(chosen)
            case 1:
                self._male_idx += len(chosen)
        return chosen


class BaseUserViewer(HttpUser):
    abstract = True
    wait_time = between(5, 10)
    
    # TODO: use decorator
    def get(self, url: AnyHttpUrl, *, 
            headers: Optional[dict] = None, 
            catch_response: bool = True, 
            **kwargs) -> dict:
        if headers is None:
            headers = {'Authorization': f'Bearer {self._access_token}'}
        else:
            headers['Authorization'] = f'Bearer {self._access_token}'
        
        if not catch_response:
            return self.client.get(url=url, headers=headers, **kwargs).json()

        with self.client.get(url, 
                            headers=headers,
                            catch_response=True,
                            **kwargs) as r:
            result = r.json()
            if result['code'] != StatusCode.OK:
                r.failure(result['code'])
            else:
                r.success()
            return result

    def post(self, url: AnyHttpUrl, *, 
             headers: Optional[dict] = None, 
             catch_response: bool = True, 
             **kwargs) -> dict:
        if headers is None:
            headers = {'Authorization': f'Bearer {self._access_token}'}
        else:
            headers['Authorization'] = f'Bearer {self._access_token}'
        
        if not catch_response:
            return self.client.post(url=url, headers=headers, **kwargs).json()

        with self.client.post(url, 
                             headers=headers,
                             catch_response=True,
                             **kwargs) as r:
            result = r.json()
            if result['code'] != StatusCode.OK:
                r.failure(result['code'])
            else:
                r.success()
            return result


class UserViewer(BaseUserViewer):
    upool: UserPool = UserPool()
    upool.fetch_users()

    @task(10)
    def view_myinfo(self) -> None:
        r = self.get(api.user_myinfo)
        assert r['data']['uid'] == self._user.id
        assert r['data']['studentid'] == self._user.student_id
        assert r['data']['gender'] == self._user.gender
        assert r['data']['name'] == self._user.name
        
    @task(20)
    def view_buildinglist(self) -> None:
        r = self.get(api.room_buildinglist)
        if self._building_ids is None:
            self._building_ids = [row['building_id'] for row in r['data']['rows']]
        
    @task(20)
    def view_building(self) -> None:
        if self._building_ids is None:
            return
        
        building_id = random.choice(self._building_ids)
        self.get(api.room_building, params={'id': building_id}, name='/room/building?id=[id]')
    
    @task(5)
    def view_myroom(self) -> None:
        r = self.get(api.user_myroom)
        if r['code'] == StatusCode.OK:
            self.stop()
    
    @task(50)
    def room_empty(self) -> None:
        r = self.get(api.room_empty)
        if self._building_ids is not None:
            assert len(r['data']['row']) == len(self._building_ids) * 2
            
    def on_start(self) -> None:
        user = self.upool.get_one()
        if user is None:
            self.stop()
            return
        
        self._user = user
        self._login()
        
        self._building_ids: Optional[list[int]] = None
        
    def on_stop(self) -> None:
        ...
        
    def _login(self) -> None:
        r = self.client.post(api.auth_login,
                             json={"username": self._user.username,
                                   "password": self._user.password})
        self._access_token: str = r.json()['data']['access_token']


class GroupUserViewer(BaseUserViewer):
    upool: UserPool = UserPool()
    upool.fetch_users()
    idx: int = 0
    
    @task(10)
    def view_team_my(self) -> None:
        self.get(api.team_my)
    
    @task(2)
    def view_team_transfer(self) -> None:
        self.post(api.team_transfer, json={'student_id': random.choice(self._users).student_id})
        
    @task(40)
    def view_create_order(self) -> None:
        self.post(api.order_create, 
                  json={'group_id': self._group_id,
                        'building_id': random.choice(self._building_ids)})
        
    @task(10)
    def view_order_list(self) -> None:
        r = self.get(api.order_list)
        assert r['code'] == 200
        
        data = r['data']
        if (rows := data.get('rows')) is not None:
            for row in rows:
                if row['status'] == 'SUCCESS':
                    self.stop()
    
    @task(100)
    def switch_user(self) -> None:
        self._switch_user()
    
    def on_start(self) -> None:
        users = self.upool.get_multi(random.randint(1, 4))
        if users is None:
            self.stop()
            return
        self.idx += 1
        
        random.shuffle(users)        
        self._users = users
        self._login()
        self._create_team()    
        
        r = self.get(api.room_buildinglist)
        self._building_ids = [row['building_id'] for row in r['data']['rows']]
    
    def _switch_user(self, idx: Optional[int] = None) -> None:
        if idx is None:
            idx = random.randint(0, len(self._access_token_list) - 1)
    
        assert 0 <= idx < len(self._access_token_list)
        self._access_token = self._access_token_list[idx]
    
    def _login(self) -> None:
        self._access_token_list = [
            self.client.post(api.auth_login,
                             json={"username": user.username,
                                   "password": user.password})
                            .json()['data']['access_token']
            for user in self._users
        ]
        self._access_token = self._access_token_list[0]        
    
    def _create_team(self) -> None:
        r = self.post(api.team_create, json={'name': f'队伍{self.idx}',
                                             'describe': f'这里是队伍{self.idx}'})
        invite_code = r['data']['invite_code']
        for i in range(1, len(self._access_token_list)):
            self._switch_user(i)
            self.post(api.team_join, json={'invite_code': invite_code})
        
        self._group_id = r['data']['team_id']
        

if __name__ == '__main__':
    up = UserPool()
    up.fetch_users()
    print(up._male[0])
    print(up.get_multi(4))