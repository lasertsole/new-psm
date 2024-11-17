declare module 'three/examples/jsm/controls/OrbitControls' {
    class OrbitControls {
        constructor(object: THREE.Camera, domElement?: HTMLElement);
        enableDamping: boolean;
        update(): void;
      }
    export {OrbitControls};
}

declare module "three/examples/jsm/loaders/OBJLoader" {
    class OBJLoader {
        load(url: string | undefined, onLoad: (object: THREE.Object3D) => void, onProgress?: (event: ProgressEvent) => void, onError?: (event: ErrorEvent) => void): void;
    }
    export {OBJLoader};
}