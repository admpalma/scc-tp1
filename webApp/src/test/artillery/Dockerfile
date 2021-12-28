# Specify a base image
FROM node:latest

#Install some dependencies

#RUN npm install
WORKDIR /usr/app

RUN npm install -g artillery@latest
RUN npm install faker
RUN npm install -g artillery-plugin-metrics-by-endpoint
RUN npm install nodemailer



COPY ./ /usr/app
# Set up a default command
CMD ["./commands.sh"]
