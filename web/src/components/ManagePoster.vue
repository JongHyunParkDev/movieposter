<template>
  <div class="container">
    <div class="poster">
      <q-table
        class="table"
        title="Posters"
        :rows="rows"
        :columns="columns"
        row-key="name"
        selection="single"
        v-model:selected="selectedRow"
        :pagination="initialPagination"
        @selection="detailOnReset"
      >
        <template v-slot:body-cell-delete="props">
          <q-td :props="props">
            <q-btn
              color="negative"
              @click="deleteRow(props.row.id)"
              icon="delete"
              round
              dense
            />
          </q-td>
        </template>
      </q-table>
      <div class="form">
        <q-bar dark class="bg-dark text-white">
          <div class="col text-center text-weight-bold">Poster Add</div>
        </q-bar>
        <q-form class="q-gutter-md" @submit="onSubmit" @reset="onReset">
          <q-input
            filled
            v-model="name"
            dense
            label="Poster Name"
            lazy-rules
            :rules="[(val) => (val && val.length > 0) || 'Please Poster Name']"
          />
          <q-input
            filled
            v-model="color"
            dense
            label="Poster Main Color"
            lazy-rules
            :rules="[
              (val) =>
                (val !== null && val !== '') || 'Please type a color code',
              (val) =>
                /^#[0-9A-Fa-f]{6}$/.test(val) ||
                'Please enter a valid color code (e.g., #000000, #ffffff)',
            ]"
          />
          <q-file
            accept=".jpg, .png, .gif, .jpeg"
            v-model="file"
            label="Pick file"
            filled
            dense
            lazy-rules
            :rules="[(val) => val || 'Please File Attach']"
          >
            <template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
          <div>
            <q-btn label="Submit" type="submit" color="primary" />
            <q-btn
              label="Reset"
              type="reset"
              color="primary"
              flat
              class="q-ml-sm"
            />
          </div>
        </q-form>
      </div>
    </div>
    <div class="detail">
      <q-form
        v-if="selectedRow.length > 0"
        class="q-gutter-md"
        @submit="detailOnSubmit"
        @reset="detailOnReset"
      >
        <q-bar dark class="bg-dark text-white">
          <div class="col text-center text-weight-bold">
            Poster Details Files Add (must include mp3 (1), image (1 △))
          </div>
        </q-bar>
        <q-file
          accept=".jpg, .png, .gif, .jpeg, .mp3"
          v-model="detailFiles"
          label="Pick file"
          multiple
          filled
          dense
          lazy-rules
          :rules="[validateFiles]"
        />
        <div>
          <q-btn label="Submit" type="submit" color="primary" />
          <q-btn
            label="Reset"
            type="reset"
            color="primary"
            flat
            class="q-ml-sm"
          />
        </div>
      </q-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar, QTableColumn } from 'quasar';
// import { useRouter } from 'vue-router';
import { Api } from 'src/lib/Api';
import { Poster } from 'src/type/PosterTypes';
import { dateToDatetimeStr } from 'src/lib/DateUtil';

const $q = useQuasar();
const color = ref('');
const name = ref('');
const file = ref();
const initialPagination = ref({ rowsPerPage: 10 });

const detailFiles = ref([]);

const selectedRow = ref<Poster[]>([]);
const rows = ref<Poster[]>([]);
const columns = ref<QTableColumn[]>([
  {
    name: 'id',
    field: 'id',
    label: 'POSTER ID',
    align: 'left',
    required: true,
    sortable: true,
  },
  {
    name: 'fileName',
    field: 'fileName',
    label: 'FileName',
    required: true,
    sortable: true,
  },
  {
    name: 'name',
    field: 'name',
    align: 'center',
    label: '이름',
    sortable: true,
  },
  {
    name: 'color',
    field: 'color',
    label: '색상',
    align: 'center',
    sortable: true,
  },
  {
    name: 'createdDatetime',
    field: 'createdDatetime',
    align: 'right',
    label: '생성일',
    format: (val) => {
      return dateToDatetimeStr(new Date(val));
    },
  },
  { name: 'delete', label: '삭제', align: 'center', field: 'delete' },
]);

const deleteRow = async (id: number) => {
  rows.value = rows.value.filter((row) => row.id !== id);
  await Api.delete(`/poster/${id}`);

  $q.notify('API CALL 성공');
};
const onSubmit = async () => {
  const formData = new FormData();
  formData.append('file', file.value);
  formData.append('name', name.value);
  formData.append('color', color.value);

  const poster: Poster = await Api.postWithFiles('/poster', formData);
  rows.value.push(poster);

  $q.notify('API CALL 성공');
  onReset();
};

const onReset = () => {
  name.value = '';
  color.value = '';
  file.value = null;
};
const validateFiles = (files: Array<File>) => {
  if (!files || files.length < 2) {
    return 'You must upload at least 2 files.';
  }

  const mp3Files = files.filter(
    (file) => file.type === 'audio/mpeg' || file.name.endsWith('.mp3')
  );
  const imageFiles = files.filter((file) => file.type.startsWith('image/'));

  if (mp3Files.length < 1) {
    return 'You must upload at least one MP3 file.';
  }

  if (files.length !== mp3Files.length + imageFiles.length) {
    return 'Only MP3 and image files are allowed.';
  }

  return true;
};
const detailOnSubmit = async () => {
  const formData = new FormData();
  formData.append('posterId', selectedRow.value[0].id.toString());
  detailFiles.value.forEach((file: File) => {
    formData.append('files', file);
  });

  await Api.postWithFiles('/posterDetails', formData);

  $q.notify('API CALL 성공');
  detailOnReset();
};

const detailOnReset = () => {
  detailFiles.value = [];
};

onMounted(async () => {
  const posters = await Api.get('/posters');
  rows.value = posters;
});
</script>

<style lang="scss" scoped>
.container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  display: flex;

  > .poster {
    flex: 1;
    margin: 5px;
    display: flex;
    flex-direction: column;
    gap: 5px;

    > .table {
      flex: 1;
      border: 1px solid $dark;
      border-radius: 5px;
      overflow: hidden;
    }

    > .form {
      padding: 10px;
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 5px;
      border: 1px solid $dark;
      border-radius: 5px;
    }
  }
  > .detail {
    flex: 1;
    margin: 5px;
  }
}
</style>
