import {useState} from 'react'

function PostForm({onCreated}) {
    const [title, setTitle] = useState('')
    const [content, setContent] = useState('')
    const [error, setError] = useState('')
    const [submitting, setSubmitting] = useState(false)

    async function handleSubmit(event) {
        event.preventDefault()
        setSubmitting(true)
        setError('')

        try {
            const response = await fetch('/api/posts', {
                method: 'POST',
                headers: {'Content-Type' : 'application/json'},
                body: JSON.stringify({title, content}),
            })

            if(response.ok) {
                setTitle('')
                setContent('')
                onCreated()
            } else {
                const data = await response.json()
                setError(data.message)
            }
        } catch {
            setError('could not reach the server')
        } finally{
            setSubmitting(false)
        }
    }


    return (
        <form className="post-form" onSubmit={handleSubmit}>
            <input
                value={title}
                onChange={(event) => setTitle(event.target.value)}
                placeholder="Title"
            />
            <textarea
                value={content}
                onChange={(event)=> setContent(event.target.value)}
                placeholder='Content'
                rows={4}
            />
            {error && <p className="error">{error}</p>}

            <button type="submit" disabled={submitting}>
                Post
            </button>
        </form>
    )
}

export default PostForm