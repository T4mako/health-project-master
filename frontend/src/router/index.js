/*
 * @Author: daidai
 * @Date: 2022-01-12 14:22:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-28 14:53:02
 * @FilePath: \web-pc\src\pages\big-screen\router\index.js
 */
import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        redirect: '/homeIndex',
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
                path: '/people/:peopleName',
                name: 'people',
                component: () => import('../views/people/index.vue')
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
            },{
                path: '/atlas',
                name: 'atlas',
                component: () => import('../views/atlas/index.vue')
            }
        ]
    }

];
const router = new VueRouter({
    mode: "hash",
    // base: process.env.BASE_URL,
    routes
});

export default router;
