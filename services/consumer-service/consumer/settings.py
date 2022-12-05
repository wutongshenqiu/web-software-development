from pydantic import BaseSettings


class Settings(BaseSettings):
    rabbitmq_host: str = 'localhost'
    rabbitmq_port: int = 39004
    rabbitmq_default_user: str = 'qiufeng'
    rabbitmq_default_pass: str = '123456'

settings = Settings()

print(settings)
