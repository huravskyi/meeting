<template>
    <v-autocomplete
            autocomplete="off"
            type="text"
            name="locateUser"
            v-model="locale"
            label="Город"
            :items="items"
            :loading="isLoading"
            :search-input.sync="search"
            hide-no-data
            hide-selected
            item-text="name"
            prepend-icon="mdi-database-search"
            placeholder="Напечатайте первые буквы"
            return-object
            clearable
            required
            :rules="rules? cityRules:[]"

    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    Найдите свой город
                    <strong>Город</strong>
                </v-list-item-title>
            </v-list-item>
        </template>
        <template v-slot:item="{ item }">
            <v-list-item-avatar
                    color="indigo"
                    class="headline font-weight-light white--text"
            >
                <div style="margin: auto">{{item.name.charAt(0)}}</div>
            </v-list-item-avatar>
            <v-list-item-content>
                <v-list-item-title v-text="item.name"></v-list-item-title>
                <v-list-item-subtitle v-text="">{{getDescriptionCity(item.parent_id)}}
                </v-list-item-subtitle>
            </v-list-item-content>
        </template>
    </v-autocomplete>
</template>
<script>
    import {getDescription} from "../../util/helper/functionLodash";

    const cityJson = ('https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/city.json?alt=media&token=4f17a64e-4a8b-47b3-950d-54f50f1b88ac')
    export default {
        props: {
            localeSearch: String,
            rules: Boolean,
        },
        name: "AutocompleteCity",
        data: () => ({
            cityRules: [v => !!v || 'Город обязательно'],
            type: false,
            cityJson,
            entries: [],
            search: null,
            isLoading: false,
            locale: null,
             des: null
        }),
        computed: {
            items() {
                let items = this.entries
                this.entries.forEach(region => {
                    let reg = region.areas
                    items = items.concat(reg)
                    reg.forEach(city => {
                        items = items.concat(city.areas)
                    })
                })
                return items
            },
        },
        mounted() {
            if (this.search === null && this.localeSearch !== 'null') {
                this.locale = this.localeSearch
                this.search = this.localeSearch
            }
        },
        watch: {
            search(val) {
                if (this.items.length > 1) return
                if (this.isLoading) return
                this.isLoading = true
                this.getCity()
            },
        },
        methods: {
            getDescriptionCity(parent_id) {
                return getDescription(parent_id, this.entries)
            },
            async getCity() {
                await fetch(this.cityJson)
                    .then(res => res.json())
                    .then(res => {
                        this.entries = res
                    })
                    .catch(err => {
                        console.log(err)
                    })
                    .finally(() => (this.isLoading = false))
            },
        }
    }
</script>

<style>

</style>