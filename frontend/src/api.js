async function request(path, options) {
    const response = await fetch(`/api${path}`, options)
    if(!response.ok) {
        const data = await response.json().catch(() => ({}))
        throw new Error(data.message ?? `Request faield (${response.status})`)
    }
        return response
}

export function createPost(title, content) {
    return request('/posts', {
        method: 'POST',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify({title, content}),
        })
}

export function deletePost(id) {
    return request(`/posts/${id}`, {method: 'DELETE'})
}

export function updatePost(id, title, content) {
    return request(`/posts/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body:JSON.stringify({title, content}),
        })
}

export async function fetchComments(postId) {
    const response = await request(`/posts/${postId}/comments`)
    return response.json()
}

export function createComment(postId, content) {
    return request(`/posts/${postId}/comments`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}, 
        body: JSON.stringify({content}),
    })
}

export function deleteComment(postId, commentId) {
    return request(`/posts/${postId}/comments/${commentId}`, {method : 'DELETE'})
}