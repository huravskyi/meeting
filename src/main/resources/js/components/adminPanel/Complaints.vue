<template>
<!--        <div style='display: flex;'>-->
<!--            <h1 STYLE='margin:0; color: #0a70ff'>Dating World</h1>-->
<!--            <img width='35px' height='35px '-->
<!--                 src='%s'/>\n" +-->
<!--        </div>-->
<!--        \n" +-->
<!--        <p>Пользователь <span style='color: blue'>%s</span>-->
<!--            Ваша анкета заблокирована /p>-->
<!--            </div>-->

    <!--    <v-card dark width="300" height="300" color="blue">-->
    <!--        <h1>DW</h1>-->
    <!--    </v-card>-->
    <!--    <v-card-->

    <!--    >-->
    <!--        <v-card-title class="headline red lighten-3">-->
    <!--            Search for Public APIs-->
    <!--        </v-card-title>-->
    <!--        <v-card-text>-->
    <!--            Explore hundreds of free API's ready for consumption! For more information visit-->
    <!--            <a-->
    <!--                    class="grey&#45;&#45;text text&#45;&#45;lighten-3"-->
    <!--                    href="https://github.com/toddmotto/public-apis"-->
    <!--                    target="_blank"-->
    <!--            >the Github repository</a>.-->
    <!--        </v-card-text>-->
    <!--        <v-card-text>-->
    <!--            <v-autocomplete-->
    <!--                    v-model="model"-->
    <!--                    :items="items"-->
    <!--                    :loading="isLoading"-->
    <!--                    :search-input.sync="search"-->
    <!--                    color="white"-->
    <!--                    hide-no-data-->
    <!--                    hide-selected-->
    <!--                    item-text="Description"-->
    <!--                    item-value="API"-->
    <!--                    label="Public APIs"-->
    <!--                    placeholder="Start typing to Search"-->
    <!--                    prepend-icon="mdi-database-search"-->
    <!--                    return-object-->
    <!--            ></v-autocomplete>-->
    <!--        </v-card-text>-->
    <!--        <v-divider></v-divider>-->
    <!--&lt;!&ndash;        <v-expand-transition>&ndash;&gt;-->
    <!--&lt;!&ndash;            <v-list v-if="model" class="red lighten-3">&ndash;&gt;-->
    <!--&lt;!&ndash;                <v-list-item&ndash;&gt;-->
    <!--&lt;!&ndash;                        v-for="(field, i) in fields"&ndash;&gt;-->
    <!--&lt;!&ndash;                        :key="i"&ndash;&gt;-->
    <!--&lt;!&ndash;                >&ndash;&gt;-->
    <!--&lt;!&ndash;                    <v-list-item-content>&ndash;&gt;-->
    <!--&lt;!&ndash;                        <v-list-item-title v-text="field.value"></v-list-item-title>&ndash;&gt;-->
    <!--&lt;!&ndash;                        <v-list-item-subtitle v-text="field.key"></v-list-item-subtitle>&ndash;&gt;-->
    <!--&lt;!&ndash;                    </v-list-item-content>&ndash;&gt;-->
    <!--&lt;!&ndash;                </v-list-item>&ndash;&gt;-->
    <!--&lt;!&ndash;            </v-list>&ndash;&gt;-->
    <!--&lt;!&ndash;        </v-expand-transition>&ndash;&gt;-->
    <!--        <v-card-actions>-->
    <!--            <v-spacer></v-spacer>-->
    <!--            <v-btn-->
    <!--                    :disabled="!model"-->
    <!--                    color="grey darken-3"-->
    <!--                    @click="model = null"-->
    <!--            >-->
    <!--                Clear-->
    <!--                <v-icon right>mdi-close-circle</v-icon>-->
    <!--            </v-btn>-->
    <!--        </v-card-actions>-->
    <!--    </v-card>-->
</template>

<script>

    export default {
        name: "Complaints",
        data: () => ({
            descriptionLimit: 60,
            entries: [],
            isLoading: false,
            model: null,
            search: null,
        }),


        computed: {
            fields() {
                if (!this.model) return []

                return Object.keys(this.model).map(key => {
                    return {
                        key,
                        value: this.model[key] || 'n/a',
                    }
                })
            },
            items() {
                return this.entries.map(entry => {
                    console.log(entry.Description)
                    const Description = entry.Description.length > this.descriptionLimit
                        ? entry.Description.slice(0, this.descriptionLimit) + '...'
                        : entry.Description

                    return Object.assign({}, entry, {Description})
                })
            },
        },

        watch: {
            search(val) {
                // Items have already been loaded
                if (this.items.length > 0) return

                // Items have already been requested
                if (this.isLoading) return

                this.isLoading = true

                // Lazily load input items
                fetch('https://api.publicapis.org/entries')
                    .then(res => res.json())
                    .then(res => {
                        console.log(res)
                        const {count, entries} = res
                        this.count = count
                        this.entries = entries
                    })
                    .catch(err => {
                        console.log(err)
                    })
                    .finally(() => (this.isLoading = false))
            },
        },
    }
</script>

<style scoped>
</style>