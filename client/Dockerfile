# ----------------------------
# mar7ala 1: tachron dyal front smito (Node.js)
# (tkhaylo Node.JS b7al dak mostaj mol rmok li kaygol kob lia l7lobn)
# ----------------------------
FROM node:22-alpine AS builder

# hna ghadi ngolo lih '/app' howa milaf l3amal
WORKDIR /app


# ancopiw lmalafat dyal package (better caching) on7tohom fl container
COPY package.json package-lock.json ./

# ta7mil al dependencieز dakhil lcontainer
RUN npm ci

# ancopiw ba9i lmalafat dakhil lcontainer
COPY . .

# haaaaa hna ghadi ngolo l3mi tachron (Node.js) ibni lina chi react app mli7
# ghadi imchi ibnihom f wa7ed lbo93a (folder) smitha (/dist)
RUN npm run build

# ----------------------------
# Mar7ala 2 : The Srbay (serveur) (NGINX)
# ----------------------------
FROM nginx:alpine

# 7yed 3lia dik page d'acceuil d NGINX 7altha 7ala
RUN rm -rf /usr/share/nginx/html/*

# jib lia dakchi li bna lina 3mi Node.Js o7to lia fNginx bach isrbih l3ibad lah
COPY --from=builder /app/dist /usr/share/nginx/html

# 7yd 3lia dik config dyal l3bar li dayrin malin nginx , 
# odir lia config lmli7a dyalna li deja 7tinaha f client/nginx.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Ara bra3 3ti lnass tchof tadawol ass7aybi fport 80
EXPOSE 80

# dimari lia dak nginx ass7aybi 
CMD ["nginx", "-g", "daemon off;"]