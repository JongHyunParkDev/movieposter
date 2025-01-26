import { ref } from 'vue';
import { defineStore } from 'pinia';
import { PosterDetailFile } from 'src/type/PosterTypes';

export const useFileStore = defineStore({
  id: 'file',
  state: () => ({
    files: ref<Array<PosterDetailFile>>([]),
  }),
  actions: {
    add(posterDetailFile: PosterDetailFile) {
      this.files.push(posterDetailFile);
    },
    clear() {
      this.files = [];
    },
  },
});
