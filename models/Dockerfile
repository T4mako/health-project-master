# 使用官方的Python镜像作为基础镜像
FROM python:3.11.10

# 设置工作目录
WORKDIR /app

# 将当前目录的内容复制到容器的工作目录中
COPY . /app/

# 安装pip并更新到最新版本
RUN pip install --upgrade pip -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple

# 安装项目依赖
# 这里假设你有一个 requirements.txt 文件，其中列出了所有的Django依赖
RUN pip install -r requirements.txt -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple

# 暴露运行Django的端口（默认8000）
EXPOSE 8000

# 设置环境变量，指定Django的配置文件
ENV DJANGO_SETTINGS_MODULE=healthmodel.settings

# 启动Django应用
CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]
