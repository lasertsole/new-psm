import type { Reactive } from "vue";
import type { UserInfo } from "@/types/user";
import type { Response } from "@/types/request";

/**
 * 用户信息
 * 
 * @author: moye
 * @returns Reactive
 */
export const userInfo: Reactive<UserInfo> = reactive<UserInfo>({
    id: '0',
    name: '',
    hasPass: undefined,
    phone: '',
    email: '',
    avatar: import.meta.env.Default_User_Avatar,
    profile: '',
    sex: undefined,
    createTime: '',
    isAdmin: false,
    isLogin: false,
    // isIdle: true,
    // canUrgent: false
});

/**
 * 更新用户数据
 * 
 * @author: moye
 * @param data 要更新的数据对象
 */
function updateUserInfo(data:UserInfo): void{
    // 将 sessionStorage中的userInfo删除
    sessionStorage.removeItem('userInfo');
    
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

    // 将 userInfo 存储到 sessionStorage
    sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
};

function copyUserInfoFromSessionStorage(): boolean{
    const storedUserInfo = sessionStorage.getItem("userInfo");
    if (storedUserInfo) {
        Object.assign(userInfo, JSON.parse(storedUserInfo));
        return true;
    } else {
        return false;
    };
};

function clearUserInfo(): void{
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

    // 将 sessionStorage中的userInfo删除
    sessionStorage.removeItem('userInfo');
};

function loginApi(data:UserInfo): void{
    userInfo.isLogin = true;
    updateUserInfo(data);
    navigateTo("/home");
};

function logoutApi(): void{
    userInfo.isLogin = false;
    clearUserInfo();
    navigateTo("/");
};

export async function login(name:string | undefined, password:string | undefined):Promise<boolean>{
    try {
        //先清除token防止旧token影响
        import.meta.client&&localStorage.removeItem('token');
        
        const res:Response = await fetchApi({
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
            import.meta.client&&ElMessage.error('登录失败:'+ msg);
            import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
            userInfo.isLogin = false;

            return false;
        }

        let data = res.data;
        loginApi(data);

        import.meta.client&&ElMessage.success('登录成功');

        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('登录失败');
        return false;
    }
};

export async function fastLogin():Promise<boolean>{
    try{
        if(import.meta.client&&!localStorage.getItem('token')) return false;
        if(copyUserInfoFromSessionStorage()){
            import.meta.client&&ElMessage.success('登录成功');
            return true;
        }
        
        const res:Response = await fetchApi({
            url: '/users/fastLogin',
            opts: {
            },
            method: 'get',
            contentType: 'application/json',
        });
        
        if(res == null || res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('登录失败:'+ msg);
            import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
            userInfo.isLogin = false;
            return false;
        }
    
        let data = res.data;
        loginApi(data);
    
        import.meta.client&&ElMessage.success('登录成功');
        emit("online");
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('登录失败');
        import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        
        return false;
    }
};

export async function thirdPartyLogin(path:string):Promise<void>{
    try{
        let opts = useRuntimeConfig().public;

        import.meta.client&&(window.location.href = opts.baseURL + opts.oauth2AuthURL + "/gitee");
    }
    catch (error) {
        import.meta.client&&ElMessage.error('第三方登录失败');
    }
};

export async function register(name:string, password:string, email:string):Promise<Boolean>{
    try{
        const res:Response = await fetchApi({
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
            import.meta.client&&ElMessage.error('登录失败:'+ msg);
            import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
    
            return false;
        }
    
        let data = res.data;
        loginApi(data);
    
        import.meta.client&&ElMessage.success('注册成功');
    
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('注册失败');
        return false;
    }
};

export async function updateAvatar(avatarFile:Blob):Promise<Boolean>{
    try{
        if(avatarFile == null || avatarFile == undefined) return false;
        const formData = new FormData();
        formData.append('oldAvatar', userInfo.avatar||"");
        formData.append('avatarFile', avatarFile);
    
        const res:Response = await fetchApi({
            url: '/users/updateAvatar',
            opts: formData,
            method: 'put',
            contentType: 'multipart/form-data',
        });
    
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('更新头像失败:'+ msg);
    
            return false;
        }
    
        let data = res.data;
        userInfo.avatar = data;
    
        import.meta.client&&ElMessage.success('更新头像成功');
    
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('更新头像失败');
        
        return false;
    }
};

export async function updateAccountInfo({name, sex, phone, email, profile}:UserInfo):Promise<Boolean>{
    try{
        const res:Response = await fetchApi({
            url: '/users/updateInfo',
            opts: {
                name,
                sex,
                phone,
                email,
                profile
            },
            method: 'put',
            contentType: 'application/json',
        });
    
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('更新账户信息失败:'+ msg);
    
            return false;
        }
    
        const data:UserInfo = {name, sex, phone, email, profile};
        updateUserInfo(data);
        
        import.meta.client&&ElMessage.success('更新账户信息成功');
    
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('更新账户信息失败');

        return false;
    }
};

export async function updatePassword(password : string, changePassword: string):Promise<Boolean>{
    try{
        const res:Response = await fetchApi({
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
    
        import.meta.client&&localStorage.removeItem('token');
        logoutApi();
    
        import.meta.client&&ElMessage.success('更新密码成功');
        
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('更新密码失败');
        return false;
    }
};

export async function logout():Promise<boolean>{
    try{
        const res:Response = await fetchApi({
            url: '/users/logout',
            method: 'delete',
        });
        
        if(res.code!=200){
            ElMessage.error('登出失败:'+res.msg);
    
            return false;
        }
    
        import.meta.client&&localStorage.removeItem('token');
        logoutApi();
    
        import.meta.client&&ElMessage.success('登出成功');
    
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('登出失败');
        return false;
    }
};