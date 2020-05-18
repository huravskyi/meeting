


 export default function getMyAge(myData) {
    if(myData === null)return ''
    let yearOfBirth = myData.slice(0, 4)
    let monthOfBirth = myData.slice(5, 7)
    let dateOfBirth = myData.slice(8, 10)
    return countAge(yearOfBirth, monthOfBirth, dateOfBirth)

}

function countAge(yearOfBirth, monthOfBirth, dateOfBirth) {
    let now = new Date()
    let today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    let dob = new Date(yearOfBirth, monthOfBirth-1, dateOfBirth)
    let dobnow = new Date(today.getFullYear(), dob.getMonth(), dob.getDate())
    let age

    age = today.getFullYear() - dob.getFullYear()
    if (today < dobnow) {
        age = age - 1;
    }
    return age
}