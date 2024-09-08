import type { UserInfo } from "../types/user";

export const userInfo = reactive<UserInfo>({
    id: '0',
    name: '',
    email: '',
    avatar: '',
    profile: '',
    sex: 0,
    createTime: '',
    isAdmin: false,
    isLogin: false,
})

export async function login(name:string | undefined, password:string | undefined):Promise<Boolean>{
    //先清除token防止旧token影响
    localStorage.removeItem('token');
    
    const res:any = await useFetchApi({
        url: '/users/login',
        opts: {
            name,
            password,
        },
        method: 'post',
        contentType: 'application/json',
    });

    if(res.code!=200){
        ElMessage.error('登录失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        // localStorage.setItem('online', 'false');
        userInfo.isLogin = false;

        return false;
    }

    ElMessage.success('登录成功');

    let data = res.data;

    userInfo.isLogin = true;
    data.isAdmin && (userInfo.isAdmin = res.data.isAdmin);
    data.id && (userInfo.id = res.data.id);
    data.name && (userInfo.name = res.data.name);
    data.email && (userInfo.email = res.data.email);
    data.avatar && (userInfo.avatar = res.data.avatar);
    data.profile && (userInfo.profile = res.data.profile);
    data.sex && (userInfo.sex = res.data.sex);
    data.createTime && (userInfo.createTime = res.data.createTime);

    return true;
}

export async function fastLogin():Promise<Boolean>{
    const res:any = await useFetchApi({
        url: '/users/fastLogin',
        opts: {
        },
        method: 'get',
        contentType: 'application/json',
    });
    
    if(res.code!=200){
        ElMessage.error('登录失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        localStorage.setItem('online', 'false');
        return false;
    }

    ElMessage.success('登录成功');

    let data = res.data;

    userInfo.isLogin = true;
    data.isAdmin && (userInfo.isAdmin = res.data.isAdmin);
    data.id && (userInfo.id = res.data.id);
    data.name && (userInfo.name = res.data.name);
    data.email && (userInfo.email = res.data.email);
    data.avatar && (userInfo.avatar = res.data.avatar);
    data.profile && (userInfo.profile = res.data.profile);
    data.sex && (userInfo.sex = res.data.sex);
    data.createTime && (userInfo.createTime = res.data.createTime);

    return true;
}

export async function thirdPartyLogin(path:string):Promise<void>{
    window.location.href = useRuntimeConfig().public.baseURL + useRuntimeConfig().public.oauth2AuthURL + "/gitee";
}

export async function register(name:string, password:string, email:string):Promise<Boolean>{
    const res:any = await useFetchApi({
        url: '/users/register',
        opts: {
            name,
            password,
            email
        },
        method: 'post',
        contentType: 'application/json',
    });

    if(res.code!=200){
        ElMessage.error('注册失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        // localStorage.setItem('online', 'false');

        return false;
    }

    ElMessage.success('注册成功');

    let data = res.data;

    userInfo.isLogin = true;
    data.isAdmin && (userInfo.isAdmin = res.data.isAdmin);
    data.id && (userInfo.id = res.data.id);
    data.name && (userInfo.name = res.data.name);
    data.email && (userInfo.email = res.data.email);
    data.avatar && (userInfo.avatar = res.data.avatar);
    data.profile && (userInfo.profile = res.data.profile);
    data.sex && (userInfo.sex = res.data.sex);
    data.createTime && (userInfo.createTime = res.data.createTime);

    return true;
}

export async function logout():Promise<Boolean>{
    const res:any = await useFetchApi({
        url: '/users/register',
        method: 'post',
    });
    if(res.code!=200){
        ElMessage.error('登出失败:'+res.msg);

        return false;
    }

    ElMessage.success('登出成功');
    if(process.client)
    {
        localStorage.removeItem('token');
        localStorage.setItem('online', 'false');
    }

    return true;
}