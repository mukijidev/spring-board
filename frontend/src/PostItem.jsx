import {useState} from 'react'

function PostItem({post, onDeleted}) {
    const [open, setOpen] = useState(false)
    const [error, setError] = useState('')

    async function handleDelete() {
        if(!window.confirm('Delete this post?')){
            return
        }
        setError('')

        try {
            const response = await fetch(`/api/posts/${post.id}`, {method: 'DELETE'})
            if(response.ok) {
                onDeleted()
            } else {
                const data = await response.json()
                setError(data.message)
            }
        } catch (e) {
            console.error(e)
            setError('Could not reach the server')
        }

    }


    return (
        <li className="post-item">
            <button className="post-title" onClick={() => setOpen(!open)}>
                {open ? '-' : '+'} {post.title}
            </button>
            {open && (
                <div className="post-detail">
                    <p className="post-date">{new Date(post.createdAt).toLocaleString()}</p>
                    <p className="post-content">{post.content}</p>
                    {error && <p className="error">{error}</p>}
                    <button onClick={handleDelete}>Delete</button>
                </div>
            )}
        </li>
    )
}

export default PostItem