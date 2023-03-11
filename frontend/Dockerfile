FROM node:18

## https://stackoverflow.com/questions/44796056/whats-the-better-approach-serving-static-files-with-express-or-nginx
## When productionising, use nginx to serve not node
#
#RUN npm install -g serve
#WORKDIR /app
#
#CMD serve

COPY . .
WORKDIR /app

# TODO: Install first
RUN npm install
CMD npm run start
