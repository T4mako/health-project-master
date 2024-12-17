# 构建阶段：使用 Node.js 镜像来构建应用
FROM node:16 AS build

# 设置工作目录
WORKDIR /app

# 将源代码复制到容器中
COPY . .

RUN npm config set registry https://registry.npmmirror.com/

# 安装依赖
RUN npm install

# 构建 Vue.js 应用（生产模式）
RUN npm run build

# 部署阶段：使用 Nginx 镜像来托管构建好的静态文件
FROM nginx:latest

# 删除默认配置文件
RUN rm /etc/nginx/conf.d/default.conf

# 复制自定义 Nginx 配置文件到容器中
COPY nginx.conf /etc/nginx/conf.d/

# 将 Vue.js 构建产物从构建镜像复制到 Nginx 的默认网站目录
COPY --from=build /app/dist-production /srv/www

# 暴露 Nginx 默认端口 80
EXPOSE 80

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]
