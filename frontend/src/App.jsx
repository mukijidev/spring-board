import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [posts, setPosts] = useState([])

  useEffect(() => {
    async function loadPosts() {
      const response = await fetch('/api/posts?size=1')
      const data = await response.json()
      setPosts(data.content)
    }
    loadPosts()
  }, [])

  return (
    <main>
      <h1>Spring Board</h1>
      <ul>
        {posts.map((post) => (
          <li key={post.id}>{post.title}</li>
        ))}
      </ul>
    </main>
  )
}

export default App