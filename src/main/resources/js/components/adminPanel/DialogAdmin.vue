<template>
    <div class="text-center">
        <v-dialog
                v-model="dialog"
                width="500"
        >
            <v-card>
                <v-card-title
                        class="headline grey lighten-2"
                        primary-title
                >
                   {{text}}
                </v-card-title>
                <v-divider></v-divider>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <div style="width: 100%" class=" d-flex justify-space-between">
                        <v-btn
                                color="red"
                                text
                                @click="deleteAndWarning()"
                        >
                            Подтвердить !
                        </v-btn>
                        <v-btn
                                color="green"
                                text
                                @click="$emit('dialog-event', false)"
                        >
                            Отменна
                        </v-btn>
                    </div>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    export default {
        props: ['deleteAndWarning', "dialog", 'text'],
        name: "DialogAdmin",
    }
</script>

<style scoped>

</style>