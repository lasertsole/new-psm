export type UserInfo = {
    id?: string | undefined;
    name?: string;
    phone?: string;
    password?: string;
    repassword?: string;
    email?: string;
    avatar?: string;
    profile?: string;
    isAdmin?: boolean;
    isLogin?: boolean;
    sex?: boolean;//0:男 1:女
    createTime?: string;
};