import type { UserInfo } from "../types/user";

export const userInfo = reactive<UserInfo>({
    id: '0',
    name: '',
    hasPass: undefined,
    phone: '',
    email: '',
    avatar: '/images/defaultAvatar.png',
    profile: '',
    sex: undefined,
    createTime: '',
    isAdmin: false,
    isLogin: false,
});

function updateUserInfo(data:UserInfo){
    data.isAdmin && (userInfo.isAdmin = data.isAdmin);
    data.id && (userInfo.id = data.id);
    data.name && (userInfo.name = data.name);
    data.hasPass && (userInfo.hasPass = data.hasPass);
    data.phone && (userInfo.phone = data.phone);
    data.email && (userInfo.email = data.email);
    data.avatar && (userInfo.avatar = data.avatar);
    data.profile && (userInfo.profile = data.profile);
    data.sex != undefined && (userInfo.sex = data.sex);
    data.createTime && (userInfo.createTime = data.createTime);
}

function clearUserInfo(){
    userInfo.id = '';
    userInfo.name = '';
    userInfo.hasPass = undefined;
    userInfo.email = '';
    userInfo.phone = '';
    userInfo.avatar = '/images/defaultAvatar.png';
    userInfo.profile = '';
    userInfo.sex = undefined;
    userInfo.createTime = '';
    userInfo.isAdmin = false;
}

function loginApi(data:UserInfo){
    userInfo.isLogin = true;
    updateUserInfo(data);
    navigateTo("/");
}

function logoutApi(){
    userInfo.isLogin = false;
    clearUserInfo();
    navigateTo("/");
}

export async function login(name:string | undefined, password:string | undefined):Promise<boolean>{
    //先清除token防止旧token影响
    localStorage.removeItem('token');
    
    const res:any = await fetchApi({
        url: '/users/login',
        opts: {
            name,
            password,
        },
        method: 'post',
        contentType: 'application/json',
    });

    if(res == null || res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('登录失败:'+ msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        userInfo.isLogin = false;

        return false;
    }

    ElMessage.success('登录成功');

    let data = res.data;
    loginApi(data);

    return true;
}

export async function fastLogin():Promise<boolean>{
    const res:any = await fetchApi({
        url: '/users/fastLogin',
        opts: {
        },
        method: 'get',
        contentType: 'application/json',
    });
    
    if(res == null || res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('登录失败:'+ msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        userInfo.isLogin = false;
        return false;
    }

    ElMessage.success('登录成功');

    let data = res.data;
    loginApi(data);

    return true;
}

export async function thirdPartyLogin(path:string):Promise<void>{
    let opts = useRuntimeConfig().public;
    window.location.href = opts.baseURL + opts.oauth2AuthURL + "/gitee";
}

export async function register(name:string, password:string, email:string):Promise<Boolean>{
    const res:any = await fetchApi({
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
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('登录失败:'+ msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token

        return false;
    }

    ElMessage.success('注册成功');

    let data = res.data;
    loginApi(data);

    return true;
}

export async function updateAvatar(avatar:Blob):Promise<Boolean>{
    const formData = new FormData();
    formData.append('oldAvatarUrl', userInfo.avatar||"");
    formData.append('avatar', avatar);

    const res:any = await fetchApi({
        url: '/users/updateAvatar',
        opts: formData,
        method: 'put',
        contentType: 'multipart/form-data',
    });

    if(res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('更新头像失败:'+ msg);

        return false;
    }

    ElMessage.success('更新头像成功');

    let data = res.data;
    userInfo.avatar = data;

    return true;
}

export async function updateAccountInfo({name, sex, phone, email}:UserInfo):Promise<Boolean>{
    const res:any = await fetchApi({
        url: '/users/updateInfo',
        opts: {
            name,
            sex,
            phone,
            email
        },
        method: 'put',
        contentType: 'application/json',
    });

    if(res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('更新账户信息失败:'+ msg);

        return false;
    }

    ElMessage.success('更新账户信息成功');

    const data:UserInfo = {name, sex, phone, email};
    updateUserInfo(data);
    
    return true;
}

export async function updatePassword(password : string, changePassword: string):Promise<Boolean>{
    const res:any = await fetchApi({
        url: '/users/updatePassword',
        opts: {
            password,
            changePassword
        },
        method: 'put',
        contentType: 'application/json',
    });

    if(res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('更新密码失败:'+ msg);

        return false;
    }

    ElMessage.success('更新密码成功');
    localStorage.removeItem('token');
    logoutApi();
    
    return true;
}

export async function logout():Promise<boolean>{
    const res:any = await fetchApi({
        url: '/users/logout',
        method: 'delete',
    });
    if(res.code!=200){
        ElMessage.error('登出失败:'+res.msg);

        return false;
    }

    ElMessage.success('登出成功');
    localStorage.removeItem('token');
    logoutApi();

    return true;
}