import type { Reactive } from "vue";
import type { UserInfo } from "@/types/user";
import type { Response, UseFetchResponse } from "@/types/response";

/**
 * 用户信息
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
    isIdle: false,
    canUrgent: false
});

/**
 * 更新用户数据
 * 
 * @param { UserInfo } data 要更新的数据对象
 */
function updateUserInfo(data:UserInfo): void {
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
    data.isIdle && (userInfo.isIdle = data.isIdle);
    data.canUrgent && (userInfo.canUrgent = data.canUrgent);
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
    userInfo.isIdle = false;
    userInfo.canUrgent = false;
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

/**
 * 用户登录
 * @param { string } name 用户名
 * @param { string } password 密码
 * @returns {Promise<boolean>} 登录成功返回true，失败返回false
 */
export async function login(name:string, password:string):Promise<boolean> {
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
            import.meta.client&&ElMessage.error('登录失败: '+ msg);
            import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
            userInfo.isLogin = false;

            return false;
        };

        let data = res.data;
        loginApi(data);

        import.meta.client&&ElMessage.success('登录成功');

        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('登录失败: '+error);
        return false;
    };
};


/**
 * 快速登录
 * 
 * @returns { Promise<boolean> } 登录成功返回true, 登录失败返回false
 */
export async function fastLogin():Promise<boolean> {
    try{
        if(import.meta.client&&!localStorage.getItem('token')) return false;
        
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
    } catch (error) {
        import.meta.client&&ElMessage.error('登录失败');
        import.meta.client&&localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        
        return false;
    };
};

/**
 * 第三方账号登录
 * 
 * @param { string } path 第三方账号登录路径
 */
export async function thirdPartyLogin(path:string):Promise<void>{
    try{
        let opts = useRuntimeConfig().public;

        import.meta.client&&(window.location.href = opts.baseURL + opts.oauth2AuthURL + "/gitee");
    } catch (error) {
        import.meta.client&&ElMessage.error('第三方登录失败');
    };
};

/**
 * 获取用户信息
 * 
 * @param { string } name 用户名
 * @param { string } password 密码
 * @param { string } email 邮箱
 * @returns { Promise<Boolean> } 用户信息
 */
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
    } catch (error) {
        import.meta.client&&ElMessage.error('注册失败');
        return false;
    };
};

/**
 * 上传用户头像
 * 
 * @param { Blob } avatarFile 用户头像文件
 * @returns { Promise<Boolean> } 上传成功返回true，失败返回false
 */
export async function updateAvatar(avatarFile:Blob):Promise<Boolean> {
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
        };
    
        let data = res.data;
        userInfo.avatar = data;
    
        import.meta.client&&ElMessage.success('更新头像成功');
    
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('更新头像失败');
        
        return false;
    };
};

/**
 * 更新用户信息
 * 
 * @param { string } name 用户名
 * @param { boolean } sex 性别
 * @param { string } phone 手机号
 * @param { string } email 用户邮箱
 * @param { string } profile 用户简介
 * @param { boolean } isIdle 是否空闲
 * @param { boolean }canUrgent 能否加急
 * @returns { Promise<Boolean> } 更新成功与否
 */
export async function updateAccountInfo({name, sex, phone, email, profile, isIdle, canUrgent}:UserInfo):Promise<Boolean> {
    try{
        const res:Response = await fetchApi({
            url: '/users/updateInfo',
            opts: {
                name,
                sex,
                phone,
                email,
                profile,
                isIdle,
                canUrgent
            },
            method: 'put',
            contentType: 'application/json',
        });
    
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('更新信息失败:'+ msg);
    
            return false;
        }
    
        const data:UserInfo = {name, sex, phone, email, profile};
        updateUserInfo(data);
        
        import.meta.client&&ElMessage.success('更新信息成功');
    
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('更新信息失败');

        return false;
    };
};

/**
 * 修改用户密码
 * 
 * @param { string } password 旧密码
 * @param { string } changePassword 新密码
 * @returns { Promise<Boolean> } 是否修改成功
 */
export async function updatePassword(password : string, changePassword: string):Promise<Boolean> {
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
        };
    
        import.meta.client&&localStorage.removeItem('token');
        logoutApi();
    
        import.meta.client&&ElMessage.success('更新密码成功');
        
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('更新密码失败');
        return false;
    };
};

/**
 * 登出
 * 
 * @returns {Promise<boolean>} 登出成功返回true, 登出失败返回false
 */
export async function logout():Promise<boolean>{
    try{
        const res:Response = await fetchApi({
            url: '/users/logout',
            method: 'delete',
        });
    
        import.meta.client&&localStorage.removeItem('token');
        logoutApi();
    
        import.meta.client&&ElMessage.success('登出成功');
        emit("offline");
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('登出失败');
        return false;
    };
};

// 被动强制登出
export function forcedLogout():void{
    import.meta.client&&localStorage.removeItem('token');
    logoutApi();
    emit("offline");
};

/**
 * 根据用户id获取用户信息
 * 
 * @param { string } userId 用户id
 * @returns { Promise<UserInfo|null> } 用户信息
 */
export async function getUserById(userId:string):Promise<UserInfo|null> {
    try{
        const res:Response = await fetchApi({
            url: `/users/${userId}`,
            method: 'get',
        });

        if(res.code!=200){
            ElMessage.error('获取用户信息失败:'+res.msg);
    
            return null;
        };
    
        import.meta.client&&localStorage.removeItem('token');
    
        return res.data;
    } catch (error) {
        ElMessage.error('获取用户信息失败:'+error);
        return null;
    };
};

/**
 * 根据用户id列表批量获取用户信息
 * 
 * @param { string[] } userIds 用户id列表
 * @returns { Promise<UserInfo[]> } Promise<UserInfo[]> 用户信息列表
 */
export async function getUserByIds(userIds:string[]):Promise<UserInfo[]> {
    try{
        const res:Response = await fetchApi({
            url: '/users',
            method: 'get',
            opts: {
                userIds: userIds
            }
        });
        
        if(res.code!=200){
            ElMessage.error('获取用户信息失败:'+res.msg);
    
            return [];
        };
    
        import.meta.client&&localStorage.removeItem('token');
    
        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('获取用户信息失败');
        return [];
    };
};