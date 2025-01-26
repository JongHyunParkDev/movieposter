import { ref } from 'vue';
import { defineStore } from 'pinia';

const maxNumError = 1;

export const useErrorStore = defineStore({
    id: 'error',
    state: () => ({
        errors: ref<Array<string>>([]),
    }),
    actions: {
        addError(msg: string) {
            this.errors.push(msg);
            if (this.errors.length > maxNumError)
                this.errors.shift();
        },
        clearError() {
            this.errors = [];
        }
    }
});
