# Spring ActiveMQ

ActiveMQ is a message oriented middleware. It is an open source message broker written in Java together with a full Java Message Service (JMS) client. It provides "Enterprise Features" which in this case means fostering the communication from more than one client or server.

## Start the ActiveMQ broker

run this command to start all services in the correct order.

```bash
$ docker-compose up -d
```

## JMS Topics and Queues

With JMS, you have the option of publishing messages to a Topic or Queue.
There is a fundamental difference between the two. A Topic forwards a message from the producer to many consumers at once. It’s a broadcast. This is often called Publish-and-Subscribe (Pub/Sub) messaging. A Queue may also have many consumers, but it forwards each message to only one consumer. The consumers wait in line, in a queue, taking turns getting new messages from the Queue. This often called Point-to-Point (P2P) messaging.
