import pika


def print_callback(ch, method, properties, body) -> None:
    print(f'[x] Received {body.decode()}')


if __name__ == '__main__':
    from settings import settings
    
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(
            host=settings.rabbitmq_host,
            port=settings.rabbitmq_port,
            credentials=pika.PlainCredentials(
                username=settings.rabbitmq_default_user,
                password=settings.rabbitmq_default_pass)))

    channel = connection.channel()
    channel.queue_declare(queue='order', durable=True)
    channel.basic_consume(queue='order',
                          auto_ack=True,
                          on_message_callback=print_callback)
    channel.start_consuming()
    