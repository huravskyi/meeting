<template>
    <v-row align="center"
           justify="end"
    >
        <div class="mx-3" style="min-width: 280px">

            <v-expansion-panels v-model="panel" >

                <about-me :isEditing="isEditing"
                          :description="description"
                          v-on:close-panel="panel = $event"
                          v-if="myProfile"
                >
                </about-me>
                <about-me-guest v-if="!myProfile"
                                :description="description"
                > </about-me-guest>


                <target :isEditing="isEditing"
                        :description="description"
                        v-on:close-panel="panel = $event"
                        v-if="myProfile"
                ></target>
                <target-guest v-if="!myProfile"
                              :description="description"
                ></target-guest>


                <look :isEditing="isEditing"
                      :description="description"
                      v-on:close-panel="panel = $event"
                      v-if="myProfile"
                ></look>
                <look-guest v-if="!myProfile"
                            :description="description"
                >

                </look-guest>

                <personal-information :isEditing="isEditing"
                                      :description="description"
                                      v-on:close-panel="panel = $event"
                                      v-if="myProfile"
                >
                </personal-information>
                <personal-information-guest :description="description"
                                            v-if="!myProfile"
                >
                </personal-information-guest>

                <hobby :isEditing="isEditing"
                       :description="description"
                       v-on:close-panel="panel = $event"
                       v-if="myProfile"
                >
                </hobby>
                <hobby-guest  v-if="!myProfile"
                              :description="description"

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

    export default {
        props:['myProfile'],
        name: "About",
        components: {
            HobbyGuest,
            PersonalInformationGuest,
            LookGuest, TargetGuest, AboutMeGuest, Hobby, PersonalInformation, AboutMe, Target, Look},
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
                    titleAbout: 'О себе',
                    targets: 'Цели знакомства',
                    look: 'Внешность',
                    personal: 'Личная информация',
                    hobby: 'Интересы',
                },

        }),
        watch:{
           panel(){
               let index = 0
               for(let key in this.isEditing){
                    this.isEditing[key] = index === this.panel;
                   index ++
               }
           }


        }
    }
</script>

<style scoped>

</style>