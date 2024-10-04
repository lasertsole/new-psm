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
};

export type Category = {
    oriLan: string;
    tarLan: string;
}