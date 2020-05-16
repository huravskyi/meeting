<template>

            <v-footer class="font-weight-medium  justify-center" height="50" >
                <div
                        class="d-flex align-center"
                >
                    <p class="developer text-center">
                        Разработка, продвижение сайтов -
                        <a class="developer"   href="mailto: developer.for.good.men@gmail.com">
                            developer.for.good.men@gmail.com
                        </a>
                    </p>
                    <div class="text-center pl-2">{{ new Date().getFullYear() }}</div>
                </div>
            </v-footer>

</template>

<script>
    export default {
        props: ['isMobile'],
        name: "Footer"
    }
</script>

<style scoped>


    .developer {
        margin: 0;
        text-decoration: none;
        color: black;
        font-style: italic;
        font-size: 11px;
    }
</style>