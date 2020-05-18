
export default function onPasteSmile(text, chatMessTextarea, past, e) {
    if (past === 'past') {
        if (e) {
            text.preventDefault();
            let clp = text.clipboardData;

            if (clp !== undefined) {
                text = clp.getData('text/html') || clp.getData('text/plain') || '';
            } else {
                text = window.clipboardData.getData('text') || '';
            }
        }
        if (text) {
            text = cleanUp(text);
            text = emojiToHtml(text);
            if (chatMessTextarea !== 'chatMessTextarea') {
                chatMessTextarea.innerHTML += text
                restore(chatMessTextarea)
            }
            return text

        }
    } else {
         return  htmlToEmoji(chatMessTextarea.innerHTML);

    }
}

let emojiRanges = [
    '(?:\uD83C[\uDDE6-\uDDFF]){2}', // flags
    '[\u0023-\u0039]\u20E3', // numbers
    '(?:[\uD83D\uD83C\uD83E][\uDC00-\uDFFF]|[\u270A-\u270D\u261D\u26F9])\uD83C[\uDFFB-\uDFFF]', // skin color
    '\uD83D[\uDC68\uDC69][\u200D\u200C].+?\uD83D[\uDC66-\uDC69](?![\u200D\u200C])', // family
    '[\uD83D\uD83C\uD83E][\uDC00-\uDFFF]', // surrogate pair
    '[\u3297\u3299\u303D\u2B50\u2B55\u2B1B\u27BF\u27A1\u24C2\u25B6\u25C0\u2600\u2705\u21AA\u21A9]', // simple
    '[\u203C\u2049\u2122\u2328\u2601\u260E\u261d\u2620\u2626\u262A\u2638\u2639\u263a\u267B\u267F\u2702\u2708]',
    '[\u2194-\u2199]',
    '[\u2B05-\u2B07]',
    '[\u2934-\u2935]',
    '[\u2795-\u2797]',
    '[\u2709-\u2764]',
    '[\u2622-\u2623]',
    '[\u262E-\u262F]',
    '[\u231A-\u231B]',
    '[\u23E9-\u23EF]',
    '[\u23F0-\u23F4]',
    '[\u23F8-\u23FA]',
    '[\u25AA-\u25AB]',
    '[\u25FB-\u25FE]',
    '[\u2602-\u2618]',
    '[\u2648-\u2653]',
    '[\u2660-\u2668]',
    '[\u26A0-\u26FA]',
    '[\u2692-\u269C]'
];
let emojiRegex = new RegExp(emojiRanges.join('|'), 'g');

function emojiToHtml(str) {
    str = str.replace(/\uFE0F/g, '');
    return str.replace(emojiRegex, buildImgFromEmoji);
}

let tpl = '<img style="height: 26px ; padding: 1px" class="emoji emoji--{code} js-smile-insert" src="{src}" srcset="{src} 1x, {src_x2} 2x" unselectable="on">';
let url2 = 'https://badoocdn.com/big/chat/emoji/{code}.png';
let url = 'https://badoocdn.com/big/chat/emoji@x2/{code}.png';
function buildImgFromEmoji(emoji) {
    var codePoint = extractEmojiToCodePoint(emoji);
    return $tpl(tpl, {
        code: codePoint,
        src: $tpl(url, {
            code: codePoint
        }),
        src_x2: $tpl(url2, {
            code: codePoint
        })
    });
}

function extractEmojiToCodePoint(emoji) {
    return emoji
        .split('')
        .map(function (symbol, index) {
            return emoji.codePointAt(index).toString(16);
        })
        .filter(function (codePoint) {
            return !isSurrogatePair(codePoint);
        }, this)
        .join('-');
}

function isSurrogatePair(codePoint) {
    codePoint = parseInt(codePoint, 16);
    return codePoint >= 0xD800 && codePoint <= 0xDFFF;
}

let htmlToEmojiRegex = /<img.*?class="emoji\semoji--(.+?)\sjs-smile-insert".*?>/gi;
function htmlToEmoji(html) {
    return html.replace(htmlToEmojiRegex, function (imgTag, codesStr) {
        let codesInt = codesStr.split('-').map(function (codePoint) {
            return parseInt(codePoint, 16);
        });
        let emoji = String.fromCodePoint.apply(null, codesInt);

        return emoji.match(emojiRegex) ? emoji : '';
    });
}


let tagRegex = /<[^>]+>/gim;
let styleTagRegex = /<style\b[^>]*>([\s\S]*?)<\/style>/gim;
let validTagsRegex = /<br[\s/]*>|<img\s+class="emoji\semoji[-\w\s]+"\s+((src|srcset|unselectable)="[^"]*"\s*)+>/i;

function cleanUp(text) {
    return text
        .replace(styleTagRegex, '')
        .replace(tagRegex, function (tag) {
            return tag.match(validTagsRegex) ? tag : '';
        })
        .replace(/\n/g, '');
}

function restore(chatMessTextarea) {
    let range = document.createRange();
    range.selectNodeContents(chatMessTextarea);
    range.collapse(false);
    let sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);
}

function $tpl(tpl, dict) {
    return tpl.replace(/\{([\w-]+)\}/g,
        function (str, key) {
            return dict[key] || (typeof (dict[key]) == 'number' ? 0 : '');
        }
    );
}