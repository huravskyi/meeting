<template>
    <v-row align="center"
           justify="end"
    >
        <div class="mx-3" style="width: 100%">

            <v-expansion-panels v-model="panel">

                <about-me :isEditing="isEditing"
                          :description="description"
                          :profileDetails="profileDetails"
                          v-on:close-panel="panel = $event"
                          v-if="myProfile"
                >
                </about-me>
                <about-me-guest v-if="!myProfile"
                                :description="description"
                                :profileDetailGuest="getProfileDetailGuest"
                ></about-me-guest>


                <target :isEditing="isEditing"
                        :description="description"
                        v-on:close-panel="panel = $event"
                        :profileDetails="profileDetails"
                        v-if="myProfile"
                ></target>
                <target-guest v-if="!myProfile"
                              :description="description"
                              :profileDetailGuest="getProfileDetailGuest"
                ></target-guest>

                <look :isEditing="isEditing"
                      :description="description"
                      v-on:close-panel="panel = $event"
                      :profileDetails="profileDetails"
                      v-if="myProfile"
                ></look>
                <look-guest v-if="!myProfile"
                            :description="description"
                            :profileDetailGuest="getProfileDetailGuest"
                >
                </look-guest>

                <personal-information :isEditing="isEditing"
                                      :description="description"
                                      v-on:close-panel="panel = $event"
                                      :profileDetails="profileDetails"
                                      v-if="myProfile"
                >
                </personal-information>
                <personal-information-guest :description="description"
                                            :profileDetailGuest="getProfileDetailGuest"
                                            v-if="!myProfile"
                >
                </personal-information-guest>

                <hobby :isEditing="isEditing"
                       :description="description"
                       v-on:close-panel="panel = $event"
                       :profileDetails="profileDetails"
                       v-if="myProfile"
                >
                </hobby>
                <hobby-guest v-if="!myProfile"
                             :description="description"
                             :profileDetailGuest="getProfileDetailGuest"
                ></hobby-guest>

            </v-expansion-panels>
        </div>
    </v-row>
</template>

<script>
    import Look from "./about/Look.vue";
    import Target from "./about/Target.vue";
    import AboutMe from "./about/AboutMe.vue";
    import PersonalInformation from "./about/PersonalInformation.vue";
    import Hobby from "./about/Hobby.vue";
    import AboutMeGuest from "./about/AboutMeGuest.vue";
    import TargetGuest from "./about/TargetGuest.vue";
    import LookGuest from "./about/LookGuest.vue";
    import PersonalInformationGuest from "./about/PersonalInformationGuest.vue";
    import HobbyGuest from "./about/HobbyGuest.vue";
    import {mapState} from "vuex";

    export default {
        props: ['myProfile', 'profileDetails', 'profileUserGuest'],
        name: "About",
        components: {
            HobbyGuest,
            PersonalInformationGuest,
            LookGuest, TargetGuest, AboutMeGuest, Hobby, PersonalInformation, AboutMe, Target, Look
        },
        data: () => ({
            panel: [],
            isEditing:
                {
                    about: false,
                    target: false,
                    look: false,
                    personal: false,
                    hobby: false,
                }
            ,
            description:
                {
                    titleAbout: '?? ????????',
                    targets: '???????? ????????????????????',
                    look: '??????????????????',
                    personal: '???????????? ????????????????????',
                    hobby: '????????????????',
                },

        }),
        computed: {
            getProfileDetailGuest() {
                if (this.profileUserGuest !== null) {
                    return this.profileUserGuest.profileDetail
                }
            }
        },
        watch: {
            panel() {
                let index = 0
                for (let key in this.isEditing) {
                    this.isEditing[key] = index === this.panel;
                    index++
                }
            }


        }
    }
</script>

<style scoped>

</style>