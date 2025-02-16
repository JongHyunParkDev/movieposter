import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
  },
  {
    path: '/detail',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/DetailPage.vue') }],
  },
  {
    path: '/manage',
    component: () => import('layouts/AdminLayout.vue'),
    children: [{ path: '', component: () => import('pages/ManagePage.vue') }],
  },
  {
    path: '/cookie',
    component: () => import('layouts/AdminLayout.vue'),
    children: [
      { path: 'id/:id', component: () => import('pages/SetCookiePage.vue') },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
