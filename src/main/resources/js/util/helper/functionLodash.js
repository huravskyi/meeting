import forEachLodash from 'lodash/forEach'

export  function getLocateUser(locale, entries) {
    let city = ''
    let region = ''
    let country = ''
    if (locale.parent_id == null) {
        country = locale.name
    } else {
        let countryCheck = getCountry(locale.parent_id, entries)
        if (countryCheck !== undefined && countryCheck.parent_id === null) {
            region = locale.name
            country = countryCheck.name
        } else {
            region = getRegion(locale.parent_id, entries)
            country = getCountry(region.parent_id, entries)

            city = locale.name
            region = region.name
            country = country.name
        }
    }
    return {city, region, country}
}

function getRegion(parent_id, entries) {
    let reg
    forEachLodash(entries, country => {
        forEachLodash(country.areas, region => {
            if (region.id === parent_id) {
                reg = region
                return false
            }
        })
    })
    return reg
}

function getCountry(parent_id, entries) {
    let reg
    forEachLodash(entries, country => {
        if (country.id === parent_id) {
            reg = country
            return false
        }
    })
    return reg
}

export  function getLocale(name, entries) {
    let locale
    forEachLodash(entries, country => {
        if (country.name === name) {
            locale = 'country'
            return false
        }
        forEachLodash(country.areas, region => {
            if (region.name === name) {
                locale = 'region'
                return false
            }
            forEachLodash(region.areas, city => {
                if (city.name === name) {
                    locale = 'city'
                    return false
                }
            })
        })
    })
    return locale
}

export  function getIndexForChatBlock(chatsList, user) {
    let indexChat
    forEachLodash(chatsList, (element, index) => {
        let count = 0
        element.members.forEach(itemUser => {
            if (itemUser.id === user.id || itemUser.id === userProfile.id) {
                count++
            }
        })
        if (count > 1) {
            indexChat = index
            return false
        }
    })
    return indexChat
}

export  function  getDescription(parent_id, entries) {
    let parentName = ''
    forEachLodash(entries, country => {
        if (country.id === parent_id) {
            parentName += ' ' + country.name
            return false
        }
        forEachLodash(country.areas, region => {
            if (region.id === parent_id) {
                parentName = region.name
                if (region.parent_id !== null) {
                    parentName += ', ' + getDescription(region.parent_id)
                }
                return false
            }
        })
    })
    return parentName
}
export default function getFunctionLodash () {

}