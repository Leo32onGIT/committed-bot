# Committed Bot

Discord bot for the Committed Tibia guild

On the server:
assumed working directory is: `/var/git/committed-bot`
assumed data director is: `/home`

1. Create a `prod.env` file to pass the variables to docker from: `src/main/resources/application.conf`
```
TOKEN=XXXXXXXXXXX
GUILD_ID=<DISCORD SERVER ID>
DATA_DIR=/home
```
2. Create members file and give it permissions that docker can write to:

```
touch $DATA_DIR/data/committed-bot/members/commited.dat
touch $DATA_DIR/data/committed-bot/event/0.dat
chmod 666 $DATA_DIR/data/committed-bot/members/committed.dat
chmod 666 $DATA_DIR/data/committed-bot/members/0.dat
```

Create a cronjob to update the character data every 5 minutes:

1. Install python3
```
apt install python-is-python3
apt install pip
pip install nested-lookup
```
2. Create the crontab file:
```
crontab -e
```

3. Add the following to the bottom of the file:
```
*/5 * * * * /var/git/committed-bot/event/event.py >> /home/data/committed-bot/event/history.log
```

Build the docker image and run the bot:
1. Install [SBT](https://www.scala-sbt.org/download.html):
```
apt install sbt
```

2. Install JRE:
```
apt install default-jre
```

3. Build the docker image
```
cd /var/git/committed-bot/committed-bot
sbt docker:publishLocal
```

4. Get the docker image id
```
docker images
```

Run the docker container pointing to the `<image_id>` and `prod.env` file
```
docker run --rm -d --env-file /var/git/committed-bot/prod.env -v /home/data/committed-bot:/home/data/committed-bot <image_id>
```
