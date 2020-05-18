export default function getLocate(locale, entries) {
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
    _.forEach(entries, country => {
        _.forEach(country.areas, region => {
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
    _.forEach(entries, country => {
        if (country.id === parent_id) {
            reg = country
            return false
        }
    })
    return reg
}