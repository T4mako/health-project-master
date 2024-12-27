/*
 * @Author: daidai
 * @Date: 2022-01-12 14:22:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-28 14:53:02
 * @FilePath: \web-pc\src\pages\big-screen\router\index.js
 */
import Vue from "vue";
import VueRouter from "vue-router";
import { Message } from 'element-ui';

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path:'/login',
        component: () => import('../views/login.vue')
    },
    {
        path: '/homeIndex',
        name: 'HomeIndex',
        component: () => import( '../views/HomeIndex.vue'),
    },
    {
        path: '/home',
        name: 'home',
        component: () => import(/* webpackChunkName: "LSD.bighome" */ '../views/home.vue'),
        children: [
            {
                path: '/index',
                name: 'index',
                component: () => import(/* webpackChunkName: "LSD.bighome" */ '../views/indexs/index.vue'),
            },
            {
                path: '/city/:cityName',
                name: 'city',
                component: () => import('../views/city/index.vue')
            },
            {
                path: '/people/:peopleId',
                name: 'people',
                component: () => import('../views/people/index.vue')
            },
            {
                path: '/room/:peopleId',
                name: 'room',
                component: () => import('../views/people/room.vue')
            }
        ]
    },
    {
        path: '/modelIndex',
        name: 'modelIndex',
        component: () => import('../views/model/model-index.vue'),
    },
    {
        path: '/model',
        name: 'model',
        redirect: '/data',
        component: () => import('../views/model/index.vue'),
        children: [
            {
                path: '/data',
                name: 'data',
                component: () => import('../views/model/model-1/data.vue')
            },
            {
                path: '/disease',
                name: 'disease',
                component: () => import('../views/model/model-2/data.vue')
            },
            {
                path: '/atlas',
                name: 'atlas',
                component: () => import('../views/atlas/index.vue')
            },{
                path: '/video',
                name: 'video',
                component: () => import('../views/model/video/index.vue')
            },
            {
                path: '/medic',
                name:'medic',
                component: () => import('@/views/model/medic.vue')
            }
        ]
    },
    {
        path: '/environment',
        name: 'Environment',
        redirect: '/enviromentIndex',
        component: () => import('../views/environment/Environment.vue'),
        children: [
            {
                path: '/enviromentIndex',
                name: 'EnviromentIndex',
                component: () => import('@/views/environment/EnvironmentIndex.vue')
            }
        ]
    }

];
const router = new VueRouter({
    mode: "hash",
    // base: process.env.BASE_URL,
    routes
});


// 全局路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.path !== '/login' && !token) {
        // 如果没有token，且目标路由不是登录页，则跳转到登录页
        next('/login');
        Message.warning('请先登录');
    } else {
        // 否则放行
        next();
    }
});

export default router;
