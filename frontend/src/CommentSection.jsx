import {useEffect, useState} from 'react'
import {createComment, deleteComment, fetchComments} from './api'

function CommentSection({postId, currentUser, onChanged}) {
    const [comments, setComments] = useState([])
    const [content, setContent] = useState('')
    const [error, setError] = useState('')
    const [submitting, setSubmitting] = useState(false)
    const [reloadCount, setReloadCount] = useState(0)

    useEffect(() => {
        async function loadComments() {
            try {
                const data = await fetchComments(postId)
                setComments(data.content)
            } catch(e) {
                console.error(e)
                setError(e.message)
            }
        }
        loadComments()
    }, [postId, reloadCount])

    function refresh() {
        setReloadCount((count) => count +1)
        onChanged()
    }

    async function handleSubmit(event) {
        event.preventDefault()
        setSubmitting(true)
        setError('')

        try {
            await createComment(postId, content)
            setContent('')
            refresh()
        } catch(e) {
            console.error(e)
            setError(e.message)
        } finally {
            setSubmitting(false)
        }
    }

    async function handleDelete(commentId) {
        if(!window.confirm('Delete this comment')) {
            return
        }
        
        setError('')
        try {
            await deleteComment(postId, commentId)
            refresh()
        } catch(e) {
            console.error(e)
            setError(e.message)
        }
    }

    return (
        <section className="comments">
            <h4>Comments</h4>
            {comments.length ===0 && <p className="comment-empty">No comments.</p>}
            <ul className="comment-list">
                {comments.map((comment) => (
                    <li key={comment.id} className="comment-item">
                        <span className="comment-author">{comment.authorName}</span>
                        <span>{comment.content}</span>
                        <span className="comment-date">{new Date(comment.createdAt).toLocaleString()}</span>
                        {currentUser?.username === comment.authorName && 
                        (<button onClick={()=> handleDelete(comment.id)}>Delete</button>)
                        }
                    </li>
                ))}
            </ul>
            {currentUser ? (
            <form className="comment-form" onSubmit={handleSubmit}>
                <input
                    value={content}
                    onChange={(event) => setContent(event.target.value)}
                    placeholder="Write a comment"
                />
                <button type="submit" disabled={submitting}>
                    Add
                </button>
            </form>
            ) : (
                <p className="comment-empty">Log in to comment.</p>
            )}


            {error && <p className="error">{error}</p>}
        </section>
    )
}

export default CommentSection