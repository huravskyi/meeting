

export default function scrollToElement(chat, bottomList) {
    setTimeout(() => {
        let hiddenElement = document.getElementById(bottomList);
        hiddenElement.scrollIntoView({block: "end", behavior: "smooth"});
    }, 50)
}