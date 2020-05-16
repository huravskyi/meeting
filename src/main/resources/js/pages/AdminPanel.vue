<template>
    <v-container>
        <v-card flat>
            <v-tabs v-model="tab"
                    background-color="white"
                    color="deep-purple accent-4"
                    centered
            >
                <v-tab v-for="(item, i) in tabList"
                       :key="i"
                >
                    {{item.title}}
                </v-tab>
            </v-tabs>
            <div>
                <image-admin v-if="tab === 0"></image-admin>

                <users-list-for-admin v-if="tab === 1"></users-list-for-admin>

                <complaints v-if="tab === 2"></complaints>

            </div>
        </v-card>
    </v-container>
</template>

<script>
    import ImageAdmin from "../components/adminPanel/ImageAdmin.vue";
    import UsersListForAdmin from "../components/adminPanel/UsersListForAdmin.vue";
    import Complaints from "../components/adminPanel/Complaints.vue";
    export default {
        name: "AdminPanel",
        components: {Complaints, UsersListForAdmin, ImageAdmin},
        data: () => ({
            tab: null,
            tabList: [
                {title: 'Фото'},
                {title: 'Пользователи'},
                {title: 'Жалобы'},
            ],
        }),
    }
</script>

<style scoped>
</style>