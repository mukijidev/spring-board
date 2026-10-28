import {useState} from 'react'
import { deletePost, updatePost } from './api'
import PostEditor from './PostEditor'
import CommentSection from './CommentSection'

function PostItem({post, currentUser, onDeleted, onUpdated, onCommentsChanged}) {
    const [open, setOpen] = useState(false)
    const [editing, setEditing] = useState(false)
    const [error, setError] = useState('')

    const isMine = currentUser?.username === post.authorName

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
            {post.commentCount >0 && `[${post.commentCount}]`}
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
                            <p className ="post-date"> {post.authorName} · {new Date(post.createdAt).toLocaleString()}</p>
                            <p className="post-content">{post.content}</p>                         
                            {error && <p className="error">{error}</p>}
                           {isMine && (
                            <>
                                <button onClick={() => setEditing(true)}>Edit</button>
                                <button onClick={handleDelete}>Delete</button>
                            </>
                           )}
                            <CommentSection postId={post.id} 
                            currentUser={currentUser}
                            onChanged={onCommentsChanged} />
                        </>
                    )}
                </div>
            )}
        </li>
    )
}

export default PostItem