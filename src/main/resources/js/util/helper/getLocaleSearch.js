export default function getLocale(name, entries) {
    let locale
    _.forEach(entries, country => {
        if (country.name === name) {
            locale = 'country'
            return false
        }
        _.forEach(country.areas, region => {
            if (region.name === name) {
                locale = 'region'
                return false
            }
            _.forEach(region.areas, city => {
                if (city.name === name) {
                    locale = 'city'
                    return false
                }
            })
        })
    })
    return locale
}