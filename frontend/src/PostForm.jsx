import {useState} from 'react'

function PostForm({initialTitle ='', initialContent = '', submitLabel ='Post', onSubmit}) {
    const [title, setTitle] = useState(initialTitle)
    const [content, setContent] = useState(initialContent)
    const [error, setError] = useState('')
    const [submitting, setSubmitting] = useState(false)

    async function handleSubmit(event) {
        event.preventDefault()
        setSubmitting(true)
        setError('')

        try {
            await onSubmit(title, content)
            setTitle('')
            setContent('')
        } catch (e) {
            console.error(e)
            setError(e.message)
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
                {submitLabel}
            </button>
        </form>
    )
}

export default PostForm