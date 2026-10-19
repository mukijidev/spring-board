import {useState} from 'react'

function PostItem({post}) {
    const [open, setOpen] = useState(false)

    return (
        <li className="post-item">
            <button className="post-title" onClick={() => setOpen(!open)}>
                {open ? '-' : '+'} {post.title}
            </button>
            {open && (
                <div className="post-detail">
                    <p className="post-date">{new Date(post.createdAt).toLocaleString()}</p>
                    <p className="post-content">{post.content}</p>
                </div>
            )}
        </li>
    )
}

export default PostItem