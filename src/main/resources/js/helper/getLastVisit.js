export default function getLastVisit(myData) {
    if(myData === null) return ''
    let yearOfBirth = myData.slice(0, 4)
    let monthOfBirth = myData.slice(5, 7)
    let dateOfBirth = myData.slice(8, 10)
    let date = ' ' + yearOfBirth + '-' + monthOfBirth + '-' + dateOfBirth

    let dob = new Date(yearOfBirth, monthOfBirth - 1, dateOfBirth)

    let now = new Date()
    let today = new Date(now.getFullYear(), now.getMonth(), now.getDate())

    let tmp = (today.getFullYear() - dob.getFullYear())
    if (tmp === 0) {
        tmp = ((today.getMonth()) - dob.getMonth())
        if (tmp === 0) {
            tmp = (today.getDate() - dob.getDate())
            if (tmp === 2) {
                return ' позавчера'
            } else if (tmp === 1) {
                return ' вчера'
            } else if (tmp === 0) {
                return ' сегодня'
            } else {
                return date
            }
        } else {
           let oneDay =  new Date(now - 24 * 60 * 60 * 1000)
            tmp = (oneDay.getDate() - dob.getDate())
            if (tmp === 0){
                return ' вчера'
            }
            oneDay =  new Date(now - 48 * 60 * 60 * 1000)
            tmp = (oneDay.getDate() - dob.getDate())
            if (tmp === 0){
                return ' позавчера'
            }
            return date
        }
    }
    return ' больше года назад'
}