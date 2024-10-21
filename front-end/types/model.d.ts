export type modelInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    coverUrl?: string;
    modelUrl?: string;
    cover?: Blob;
    model?: Blob;
    category?: Category;
    visible?: string;
};

export type Category = {
    style: string;
    type: string;
}