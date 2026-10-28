

// 쿠키에서 값 하나를 읽는다
function readCookie(name) {
    const row = document.cookie.split('; ').find((item) => 
    item.startsWith(`${name}=`))

    return row ? decodeURIComponent(row.split('=')[1]) : null
}

//토큰 쿠키가 없으면(처음, 로그인 직후, 로그아웃 직후) /auth/csrf로 받는다
async function csrfHeader() {
    let token = readCookie('XSRF-TOKEN')
    if(!token) {
        await fetch('/api/auth/csrf')
        token = readCookie('XSRF-TOKEN')
    }

    return {'X-XSRF-TOKEN': token}
}

//Get이 아니면 csrf헤더를 자동으로 붙인다
async function request(path, options = {}) {
    const method = options.method ?? 'GET'
    const headers = {...options.headers}

    if(method != 'GET') {
        Object.assign(headers, await csrfHeader())
    }

    const response = await fetch(`/api${path}`, {...options, headers})

    if (!response.ok) {
        const data = await response.json().catch(() => ({}))
        const error = new Error(data.message ?? `Request failed (${response.status})`)
        error.status = response.status
        throw error
    }
    return response
}

const JSON_HEADERS = {'Content-Type': 'application/json'}

// ---- 로그인 ----
// 지금 로그인한 사용자, 로그인 안했으면 null
export async function fetchMe() {
    const response = await fetch('/api/auth/me')
    if(response.status === 401) {
        return null
    }
    if(!response.ok) {
        throw new Error(`Request failed(${response.status})`)
    }
    return response.json()
}

export function signup(username, password) {
    return request(`/auth/signup`, {
        method : 'POST',
        headers: JSON_HEADERS,
        body: JSON.stringify({username, password}),
    })
}

// security의 폼 로그인은 json이 아니라 form (username =...$pawssword=...)
//body에 URLSearchParams을 넣으면 fetch가 Content-type을 form형식으로 알아서 붙인다
export async function login(username, password) {
    try {
        await request('/auth/login', {
            method: 'POST',
            body: new URLSearchParams({username, password}),
        })
    } catch(e) {
        if(e.status === 401) {
            throw new Error('Invalid username or password', {cause: e})
        }
        throw e
    }
}

export function logout() {
    return request('/auth/logout', {method:'POST'})
}

//----글----
export function createPost(title, content) {
    return request('/posts', {
        method: 'POST',
        headers: JSON_HEADERS,
        body: JSON.stringify({title, content}),
        })
}

export function deletePost(id) {
    return request(`/posts/${id}`, {method: 'DELETE'})
}

export function updatePost(id, title, content) {
    return request(`/posts/${id}`, {
        method: 'PUT',
        headers: JSON_HEADERS,
        body:JSON.stringify({title, content}),
        })
}


//----댓글----
export async function fetchComments(postId) {
    const response = await request(`/posts/${postId}/comments`)
    return response.json()
}

export function createComment(postId, content) {
    return request(`/posts/${postId}/comments`, {
        method: 'POST',
        headers: JSON_HEADERS,
        body: JSON.stringify({content}),
    })
}

export function deleteComment(postId, commentId) {
    return request(`/posts/${postId}/comments/${commentId}`, {method : 'DELETE'})
}






