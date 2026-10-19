import {useState} from 'react'
import { deletePost, updatePost } from './api'
import PostEditor from './PostEditor'

function PostItem({post, onDeleted, onUpdated}) {
    const [open, setOpen] = useState(false)
    const [editing, setEditing] = useState(false)
    const [error, setError] = useState('')

    async function handleDelete() {
        if(!window.confirm('Delete this post?')) {
            return
        }
        setError('')

        try {
            await deletePost(post.id)
            onDeleted()
        } catch(e) {
            console.error(e)
            setError(e.message)
        }
    }

    async function handleUpdate(title, content) {
        await updatePost(post.id, title, content)
        setEditing(false)
        onUpdated()
    }

    return (
        <li className="post-item">
            <button className="post-title" onClick={() => setOpen(!open)}>
            {open ? '-' : '+'} {post.title}
            </button>

            {open && (
                <div className="post-detail">
                    {editing ? (
                    <>
                        <PostEditor
                        initialTitle={post.title}
                        initialContent={post.content}
                        submitLabel="Save"
                        onSubmit={handleUpdate}
                        />
                        <button onClick={()=>setEditing(false)}>Cancel</button>
                    </>    
                    ) : (
                        <>
                            <p className ="post-date">{new Date(post.createdAt).toLocaleString()}</p>
                            <p className="post-content">{post.content}</p>                         
                            {error && <p className="error">{error}</p>}
                            <button onClick={() => setEditing(true)}>Edit</button>
                            <button onClick={handleDelete}>Delete</button>
                        </>
                    )}
                </div>
            )}
        </li>
    )
}

export default PostItem