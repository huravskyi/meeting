<template>
    <span></span>
</template>

<script>

    export default {
        props: ['functionLoader', 'list'],
        name: "LazyLoader",
        mounted() {
            window.onscroll = () => {
                const el = document.documentElement;
                const isBottomOfScreen = el.scrollTop + window.innerHeight > el.offsetHeight - 130

                if (isBottomOfScreen) {
                    const page = this.getPage()
                    if (page !== 0) {
                        this.functionLoader(page)
                    }
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        },
        methods: {

            getPage() {
                if (this.list.totalPage === 0 && this.list.currentPage === 0) return 0
                if (this.list.totalPage !== this.list.currentPage + 1) {
                    return this.list.currentPage + 1
                } else {
                    return 0
                }


            }
        }
    }
</script>

<style scoped>

</style>