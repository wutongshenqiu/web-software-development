from math import ceil
from random import randint
from datetime import datetime
from typing import Optional, Any
import abc
import functools

from pydantic import BaseModel, EmailStr, Field, AnyUrl, validator

from faker import Faker
from faker.providers import BaseProvider


class ExtraProvider(BaseProvider):
    
    def student_id(self) -> str:
        return f'2201{randint(0, 999999):6d}'
    
    def gender(self) -> int:
        return randint(0, 1)

    # TODO: elegant implementation
    def random_int_list(self, total: int, length: int, x_range: tuple[int, int]) -> list[int]:
        assert length * x_range[1] >= total
        assert x_range[0] <= x_range[1]
        
        res = []
        consume = 0
        for i in range(length):
            min_ = ceil(max(x_range[0], (total - consume) / (length - i)))
            max_ = min(x_range[1], total - consume - (length - i - 1) * x_range[0])
            x = randint(min_, max_)
            res.append(x)
            consume += x
        
        return res            
        
    

faker = Faker(locale='zh_CN')
faker.add_provider(ExtraProvider)


class BaseFakeModel(BaseModel):
    id: int
    create_time: datetime = Field(default_factory=faker.iso8601)
    update_time: datetime = Field(default_factory=faker.iso8601)
    remark: Optional[str] = None
    
    @property
    @abc.abstractmethod
    def table_name(self) -> str:
        ...
        
class FakeUserModel(BaseFakeModel):
    email: EmailStr = Field(default_factory=faker.unique.email)
    gender: int = Field(default_factory=faker.gender)
    name: str = Field(default_factory=faker.name)
    telephone: str = Field(default_factory=faker.unique.phone_number)
    user_status: int = 0
    
    @property
    def table_name(self) -> str:
        return 'tb_user'
    
class FakeStudentInfoModel(BaseFakeModel):
    user_id: int
    student_id: str = Field(default_factory=faker.unique.student_id)

    @property
    def table_name(self) -> str:
        return 'tb_student_info'


class FakeAuthModel(BaseFakeModel):
    user_id: int
    auth_status: int = 0
    auth_type: int = 0
    last_login_time: datetime = Field(default_factory=faker.iso8601)
    username: str = Field(default_factory=faker.unique.user_name)
    password: str = Field(default_factory=faker.password)
    
    @property
    def table_name(self) -> str:
        return 'tb_auth'
    
    
class FakeBuildingModel(BaseFakeModel):
    building_status: int = 0
    description: str = Field(default_factory=lambda : faker.text(200))
    image_url: AnyUrl = 'https://www.svgrepo.com/show/171828/3d-building.svg'
    name: Optional[str] = None
    
    @validator('name', always=True, pre=True)
    def set_cs(cls, v, values):
        if v is not None:
            return v
        return f"{values.get('id')} 楼"
    
    @property
    def table_name(self) -> str:
        return 'tb_building'


class FakeRoomModel(BaseFakeModel):
    building_id: int
    description: str = Field(default_factory=lambda : faker.text(200))
    gender: int = Field(default_factory=faker.gender)
    image_url: AnyUrl = 'https://www.svgrepo.com/show/149612/hotel-room.svg'
    name: Optional[str] = None
    
    @validator('name', always=True, pre=True)
    def set_cs(cls, v, values):
        if v is not None:
            return v
        return f"{values.get('building_id')}-{values.get('id')} 房" 
    
    @property
    def table_name(self) -> str:
        return 'tb_room'


class FakeBedModel(BaseFakeModel):
    room_id: int
    user_id: Optional[int] = None
    bed_status: int = 0
    name: Optional[str] = None
    
    @validator('name', always=True, pre=True)
    def set_cs(cls, v, values):
        if v is not None:
            return v
        return f"{values.get('room_id')}-{values.get('id')} 床" 
    
    @property
    def table_name(self) -> str:
        return 'tb_bed'


def gen_insert_sql(db_name: str, model: BaseFakeModel, table_name: Optional[str] = None) -> str:
    # TODO: use dispatch
    def format_value(v: Any) -> str:
        if v is None:
            return 'null'
        elif isinstance(v, str):
            return f"'{v}'"
        elif isinstance(v, int):
            return f'{v}'
        else:
            raise ValueError(f'Error when parsing value `{v}`')
    
    
    if table_name is None:
        table_name = model.table_name
    
    s = 'INSERT INTO '
    s += f'{db_name}.{table_name} '
    
    data = model.dict()
    s += f"({', '.join(data.keys())}) "
    s += 'VALUES '
    s += f"({', '.join(map(format_value, data.values()))});"
    
    return s


if __name__ == '__main__':
    from pathlib import Path
    
    TEST_USER_NUMBER = 10
    USER_NUMBER = 2000
    BUILDING_NUMBER = 10
    ROOM_NUMBER = 500
    ROOM_RANGE_PER_BUILDING = (30, 70)
    BED_NUMBER = 2200
    BED_RANGE_PER_BUILDING = (4, 6)
    
    DB_NAME = "test"
    SQL_DIR = Path('sql_data')
    SQL_DIR.mkdir(parents=True, exist_ok=True)
    
    gen_insert_sql_withdb = functools.partial(gen_insert_sql, db_name=DB_NAME)
    
    fake_user_models = [
        FakeUserModel(id=i, name=f'测试{i}')
        for i in range(1, TEST_USER_NUMBER + 1)]
    for i in range(TEST_USER_NUMBER + 1, USER_NUMBER + 1):
        fake_user_models.append(FakeUserModel(id=i))
    with (SQL_DIR / 'tb_user.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_user_models)))
    
    fake_student_info_models = [
        FakeStudentInfoModel(id=i, user_id=i, student_id=f"{i:08d}")
        for i in range(1, TEST_USER_NUMBER + 1)]
    for i in range(TEST_USER_NUMBER + 1, USER_NUMBER + 1):
        fake_student_info_models.append(FakeStudentInfoModel(id=i, user_id=i))
    with (SQL_DIR / 'tb_student_info.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_student_info_models)))
        
    fake_auth_models = [
        FakeAuthModel(id=i, user_id=i, username=f'test{i}', password="123456")
        for i in range(1, TEST_USER_NUMBER + 1)]
    for i in range(TEST_USER_NUMBER + 1, USER_NUMBER + 1):
        fake_auth_models.append(FakeAuthModel(id=i, user_id=i))
    with (SQL_DIR / 'tb_auth.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_auth_models)))
    
    building_ids = [i for i in range(1, BUILDING_NUMBER + 1)]    
    fake_building_models = [FakeBuildingModel(id=i) for i in building_ids]
    with (SQL_DIR / 'tb_building.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_building_models)))
    
    room_number_per_building = faker.random_int_list(total=ROOM_NUMBER, length=BUILDING_NUMBER, x_range=ROOM_RANGE_PER_BUILDING)
    fake_room_models = []
    _room_id = 1
    for bi in range(1, BUILDING_NUMBER + 1):
        for _ in range(room_number_per_building[bi - 1]):
            fake_room_models.append(FakeRoomModel(id=_room_id, building_id=bi))
            _room_id += 1
    with (SQL_DIR / 'tb_room.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_room_models)))            

    bed_number_per_room = faker.random_int_list(total=BED_NUMBER, length=ROOM_NUMBER, x_range=BED_RANGE_PER_BUILDING)
    fake_bed_models = []
    _bed_id = 1
    for ri in range(1, ROOM_NUMBER + 1):
        for _ in range(bed_number_per_room[ri - 1]):
            fake_bed_models.append(FakeBedModel(id=_bed_id, room_id=ri))
            _bed_id += 1
    with (SQL_DIR / 'tb_bed.sql').open('w', encoding='utf8') as f:
        f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_bed_models)))    
    
    fake_model_list = [
        fake_user_models,
        fake_student_info_models,
        fake_auth_models,
        fake_building_models,
        fake_room_models,
        fake_bed_models
    ]
    with (SQL_DIR / '.tb_all.sql').open('w', encoding='utf8') as f:
        for fake_model in fake_model_list:
            f.write('\n'.join((gen_insert_sql_withdb(model=model) for model in fake_model))) 
            f.write('\n')
    
    