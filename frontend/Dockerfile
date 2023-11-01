FROM node:18

## https://stackoverflow.com/questions/44796056/whats-the-better-approach-serving-static-files-with-express-or-nginx
## When productionising, use nginx to serve not node
#
#RUN npm install -g serve
#WORKDIR /app
#
#CMD serve

WORKDIR /app

COPY app/package*.json ./

RUN npm install

COPY . .

CMD npm run start
