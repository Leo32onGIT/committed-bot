# Committed Bot

Discord bot for the Committed Tibia guild

Build docker image  
1. `cd committed-bot`
1. `sbt docker:publishLocal`

On the server
1. Create a `prod.env` file to pass the variables to docker from: `src/main/resources/application.conf`
```
TOKEN=XXXXXXXXXXX
GUILD_ID=<DISCORD SERVER ID>
NEW_MEMBER_CHANNEL_ID=<DISCORD WELCOME CHANNEL ID>
DATA_DIR=/home
```
2. Ensure that the directory structure is setup correctly: 
```
mkdir -p $DATA_DIR/data/committed-bot/{event,members}
```
3. Create members file and give it permissions that docker can write to:

```
touch $DATA_DIR/data/committed-bot/members/commited.dat
chmod 666 $DATA_DIR/data/committed-bot/members/committed.dat
```
4. Run the docker container, pointing to the env file created in step 1:
```
docker run --rm -d --env-file prod.env -v $DATA_DIR/data/committed-bot:$DATA_DIR/data/committed-bot <image_id>
```
5. Run the event update loop:
```
while true; do ./event/event.py ; sleep 60; done
```
