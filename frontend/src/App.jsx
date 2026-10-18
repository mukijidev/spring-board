import { useEffect, useState } from 'react'
import Pagination from './Pagination'
import './App.css'


const PAGE_SIZE = 3

function App() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)


  useEffect(() => {
    async function loadPosts() {
      const response = await fetch(`/api/posts?page=${page}&size=${PAGE_SIZE}`)
      const data = await response.json()
      setPosts(data.content)
      setTotalPages(data.totalPages)
    }
    loadPosts()
  }, [page])

  return (
    <main>
      <h1>Spring Board</h1>
      <ul>
        {posts.map((post) => (
          <li key={post.id}>{post.title}</li>
        ))}
      </ul>
      <Pagination page ={page} totalPages={totalPages} onChange={setPage} />
    </main>
  )
}

export default App