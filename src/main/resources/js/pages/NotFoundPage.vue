<template>
    <v-container>
        <v-card  tile flat class="ma-auto mt-10" max-width="500">
            <v-card-title style="font-size: xx-large; color: #0a70ff" class="justify-center">
                <h1>404</h1>
            </v-card-title>
            <v-card-text>
                <h1 class="text-center">Страница не найдена</h1>
            </v-card-text>

            <v-card-actions>
                <div>
                    <p class="text-center">
                        Страница, которую вы пытаетесь найти не существует.
                        Возможно, она не была создана, либо перемещена или
                        удалена с нашего сайта.
                    </p>
                    <p class="text-center">Вы можете перейти на
                        <router-link to="/">главную</router-link>
                    </p>
                </div>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
    export default {
        name: "NotFoundPage"
    }
</script>

<style scoped>

</style>